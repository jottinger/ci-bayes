package com.enigmastation.neuralnet.impl;

import com.enigmastation.extractors.WordLister;
import com.enigmastation.extractors.impl.StemmingWordLister;
import com.enigmastation.neuralnet.Actor;
import com.enigmastation.neuralnet.NeuralNetwork;
import com.enigmastation.neuralnet.model.Neuron;
import com.enigmastation.neuralnet.model.Synapse;
import com.enigmastation.neuralnet.model.Visibility;
import com.enigmastation.neuralnet.service.NeuralNetService;
import com.gigaspaces.simpledao.dao.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("Perceptron")
public class Perceptron implements NeuralNetwork {
    @Autowired
    NeuralNetService neuralNetService;
    WordLister wordLister = new StemmingWordLister() {{
        MIN_LENGTH = 0;
    }};
    String[] ignoredWords = {"the", "a", "an", "and",};

    @Override
    public void trainNaive(String inputs, String output) {
        List<String> sortedWords = new ArrayList<String>(getWords(inputs));
        List<Neuron> outputNeurons = new ArrayList<Neuron>();
        Collections.sort(sortedWords);
        List<Neuron> inputNeurons = new ArrayList<Neuron>();
        // first, create the neurons for the input (and build the hidden neuron name)
        StringBuilder sb = new StringBuilder();
        for (String i : sortedWords) {
            inputNeurons.add(neuralNetService.getNeuron(i, Visibility.VISIBLE, true));
            sb.append(i).append(":");
        }
        // get the output neurons
        for (String i : getWords(output)) {
            Neuron outputNeuron = neuralNetService.getNeuron(i, Visibility.VISIBLE, true);
            outputNeurons.add(outputNeuron);
        }

        // last, create the neuron for the middle layer
        Neuron middleNeuron = neuralNetService.getNeuron(sb.toString(), Visibility.INVISIBLE);
        if (middleNeuron == null) {
            // no middle neuron? Create it, and assign initial strengths.
            middleNeuron = neuralNetService.getNeuron(sb.toString(), Visibility.INVISIBLE, true);
            for (Neuron inputNeuron : inputNeurons) {
                neuralNetService.setStrength(inputNeuron, middleNeuron, 1.0 / inputNeurons.size());
            }
            for (Neuron outputNeuron : outputNeurons) {
                neuralNetService.setStrength(middleNeuron, outputNeuron, 0.1);
            }
        }
    }

    private Set<String> getWords(String inputs) {
        Set<String> words = wordLister.getUniqueWords(inputs);
        for (String ignored : ignoredWords) {
            words.remove(ignored);
        }

        return words;
    }


    static Comparator<Pair<Neuron, Double>> pairComparator = new Comparator<Pair<Neuron, Double>>() {
        @Override
        public int compare(Pair<Neuron, Double> o1, Pair<Neuron, Double> o2) {
            return o2.getV().compareTo(o1.getV());

        }
    };

    public List<Pair<Neuron, Double>> getOutputs(String corpus, Actor actor) {
        List<Pair<Neuron, Double>> results = new ArrayList<Pair<Neuron, Double>>();

        List<Neuron> inputNeurons = new ArrayList<Neuron>();
        List<Neuron> hiddenNeurons = new ArrayList<Neuron>();
        List<Neuron> outputNeurons = new ArrayList<Neuron>();
        setupNeuronLists(corpus, inputNeurons, hiddenNeurons, outputNeurons);
        WeightsMatrix weightsMatrix = new WeightsMatrix(inputNeurons, hiddenNeurons, outputNeurons, actor, results).invoke();

        Collections.sort(results, pairComparator);

        return results;
    }

    @Override
    public List<Pair<Neuron, Double>> getOutputs(String corpus) {
        return getOutputs(corpus, new Actor() {
            void handle(List<Pair<Neuron, Double>> list, Neuron n, double weight) {
                list.add(new Pair<Neuron, Double>(n, weight));
            }
        });
    }

    private static double dtanh(double y) {
        return 1.0 - y * y;
    }

    /**
     * Backpropagate training with FIRST WORD FROM TARGET.
     *
     * @param corpus
     * @param target
     */
    public void backPropagate(String corpus, String target) {
        //System.out.println("bp corpus: " + corpus);
        //System.out.println("bp target: " + target);
        Actor actor = new Actor(); // we discard these results...
        Set<String> targetSequence = getWords(target);
        //System.out.println("Target Sequence: " + targetSequence);
        target = getWords(target).toArray(new String[targetSequence.size()])[0];
        //System.out.println("backpropagating with " + target);

        trainNaive(corpus, target);

        List<Neuron> inputNeurons = new ArrayList<Neuron>();
        List<Neuron> hiddenNeurons = new ArrayList<Neuron>();
        List<Neuron> outputNeurons = new ArrayList<Neuron>();
        setupNeuronLists(corpus, inputNeurons, hiddenNeurons, outputNeurons);

        Neuron targetNeuron = neuralNetService.getNeuron(target, Visibility.VISIBLE);

        WeightsMatrix weightsMatrix = new WeightsMatrix(inputNeurons, hiddenNeurons, outputNeurons, actor, null).invoke();
        double[] targets = new double[outputNeurons.size()];
        double[] outputDeltas = new double[outputNeurons.size()];
        double[] hiddenDeltas = new double[hiddenNeurons.size()];

        targets[outputNeurons.indexOf(targetNeuron)] = 1.0;

        for (Neuron output : outputNeurons) {
            double error = 0;
            int k = outputNeurons.indexOf(output);

            error = targets[k] - weightsMatrix.getOutputWeights()[k];
            outputDeltas[k] = dtanh(weightsMatrix.getOutputWeights()[k]) * error;
        }
        for (Neuron hidden : hiddenNeurons) {
            int j = hiddenNeurons.indexOf(hidden);
            double error = 0.0;
            for (Neuron output : outputNeurons) {
                int k = outputNeurons.indexOf(output);
                error += outputDeltas[k] * weightsMatrix.getWeightsHidden()[j][k];
                hiddenDeltas[j] = dtanh(weightsMatrix.getHiddenWeights()[j]) * error;
            }
        }
        for (Neuron hidden : hiddenNeurons) {
            int j = hiddenNeurons.indexOf(hidden);
            for (Neuron output : outputNeurons) {
                int k = outputNeurons.indexOf(output);
                double change = outputDeltas[k] * weightsMatrix.getHiddenWeights()[j];
                weightsMatrix.getWeightsHidden()[j][k] += 0.5 * change;
            }
        }
        for (Neuron input : inputNeurons) {
            int i = inputNeurons.indexOf(input);
            for (Neuron hidden : hiddenNeurons) {
                int j = hiddenNeurons.indexOf(hidden);
                double change = hiddenDeltas[j] * weightsMatrix.getInputWeights()[i];
                weightsMatrix.getWeightsInput()[i][j] += 0.5 * change;
            }
        }

        // now we update the freaking datastore
        for (Neuron input : inputNeurons) {
            int i = inputNeurons.indexOf(input);
            for (Neuron hidden : hiddenNeurons) {
                int j = hiddenNeurons.indexOf(hidden);
                neuralNetService.setStrength(input, hidden, weightsMatrix.getWeightsInput()[i][j]);
            }
        }

        for (Neuron hidden : hiddenNeurons) {
            int j = hiddenNeurons.indexOf(hidden);
            for (Neuron output : outputNeurons) {
                int k = outputNeurons.indexOf(output);
                neuralNetService.setStrength(hidden, output, weightsMatrix.getWeightsHidden()[j][k]);
            }
        }
    }

    private void setupNeuronLists(String corpus, List<Neuron> inputNeurons, List<Neuron> hiddenNeurons, List<Neuron> outputNeurons) {
        // okay. first, we set up the list of inputNeurons
        for (String payload : getWords(corpus)) {
            // save off this neuron, which represents a word in the corpus
            Neuron inputNeuron = neuralNetService.getNeuron(payload, Visibility.VISIBLE, true);
            if (!inputNeurons.contains(inputNeuron)) {
                inputNeurons.add(inputNeuron);
            }

            // now find all hidden outputNeurons attached to this neuron
            Set<Synapse> synapses = neuralNetService.getSynapsesFrom(inputNeuron);
            for (Synapse synapse : synapses) {
                Neuron hiddenNeuron = neuralNetService.getNeuronById(synapse.getTo());
                if (!hiddenNeurons.contains(hiddenNeuron)) {
                    hiddenNeurons.add(hiddenNeuron);
                }

                // woot, let's add the output neurons to the output set, too, while we're here
                Set<Synapse> outputSynapses = neuralNetService.getSynapsesFrom(hiddenNeuron);
                for (Synapse outputSynapse : outputSynapses) {
                    Neuron outputNeuron = neuralNetService.getNeuronById(outputSynapse.getTo());
                    if (!outputNeurons.contains(outputNeuron)) {
                        outputNeurons.add(outputNeuron);
                    }
                }
            }
        }
    }

    public Set<Neuron> getHiddenNeuronsForInput(String corpus) {
        Set<Neuron> neurons = new HashSet<Neuron>();
        for (String payload : getWords(corpus)) {
            //System.out.println("getHiddenNeuronsForInput(" + payload + ")");
            Set<Synapse> synapses = neuralNetService.getSynapsesFrom(neuralNetService.getNeuron(payload, Visibility.VISIBLE));
            //System.out.println("synapses: "+synapses);
            for (Synapse s : synapses) {
                Neuron n = neuralNetService.getNeuronById(s.getTo());
                //System.out.println("neuron: "+n);
                neurons.add(n);
            }
        }
        return neurons;
    }

    public void reset() {
        neuralNetService.reset();
    }

    @Override
    public void train(String corpus, String result) {
        trainNaive(corpus, result);
        backPropagate(corpus, result);
    }

    private class WeightsMatrix {
        private List<Neuron> inputNeurons;
        private List<Neuron> hiddenNeurons;
        private List<Neuron> outputNeurons;
        private double[] inputWeights;
        private double[] hiddenWeights;
        private double[] outputWeights;
        private double[][] weightsInput;
        private double[][] weightsHidden;
        private Actor actor;
        List<Pair<Neuron, Double>> results;

        public WeightsMatrix(List<Neuron> inputNeurons, List<Neuron> hiddenNeurons, List<Neuron> outputNeurons, Actor actor, List<Pair<Neuron, Double>> results) {
            this.inputNeurons = inputNeurons;
            this.hiddenNeurons = hiddenNeurons;
            this.outputNeurons = outputNeurons;
            this.actor = actor;
            this.results = results;
        }

        /* these are all aliases to help figure out the algorithms */
        public double[] getAI() {
            return inputWeights;
        }

        public double[] getAH() {
            return hiddenWeights;
        }

        public double[] getAO() {
            return outputWeights;
        }

        public double[][] getWI() {
            return weightsInput;
        }

        public double[][] getWO() {
            return weightsHidden;
        }

        public double[] getInputWeights() {
            return inputWeights;
        }

        public double[] getHiddenWeights() {
            return hiddenWeights;
        }

        public double[] getOutputWeights() {
            return outputWeights;
        }

        public double[][] getWeightsInput() {
            return weightsInput;
        }

        public double[][] getWeightsHidden() {
            return weightsHidden;
        }

        public WeightsMatrix invoke() {
            // now we have a set of three lists: build outputNeurons for the three lists, too
            inputWeights = new double[inputNeurons.size()];
            hiddenWeights = new double[hiddenNeurons.size()];
            outputWeights = new double[outputNeurons.size()];
            Arrays.fill(inputWeights, 1.0);
            Arrays.fill(hiddenWeights, 1.0);
            Arrays.fill(outputWeights, 1.0);
            weightsInput = new double[inputNeurons.size()][];
            weightsHidden = new double[hiddenWeights.length][];

            // now fill the inputWeights by index...
            for (Neuron input : inputNeurons) {
                int i = inputNeurons.indexOf(input);
                weightsInput[i] = new double[hiddenNeurons.size()];
                for (Neuron hidden : hiddenNeurons) {
                    int j = hiddenNeurons.indexOf(hidden);
                    weightsInput[i][j] = neuralNetService.getStrength(input, hidden, Visibility.VISIBLE);
                }
            }
            for (Neuron hidden : hiddenNeurons) {
                int i = hiddenNeurons.indexOf(hidden);
                weightsHidden[i] = new double[outputNeurons.size()];
                for (Neuron output : outputNeurons) {
                    int j = outputNeurons.indexOf(output);
                    weightsHidden[i][j] = neuralNetService.getStrength(hidden, output, Visibility.INVISIBLE);
                }
            }
            // yay, we have our matrices set up. Let's activate stuff!
            // no need to modify inputWeights...
            for (Neuron hidden : hiddenNeurons) {
                int j = hiddenNeurons.indexOf(hidden);
                double sum = 0.0;
                for (Neuron input : inputNeurons) {
                    int i = inputNeurons.indexOf(input);
                    sum += inputWeights[i] * weightsInput[i][j];
                }
                hiddenWeights[j] = Math.tanh(sum);
            }
            for (Neuron output : outputNeurons) {
                int k = outputNeurons.indexOf(output);
                double sum = 0.0;
                for (Neuron hidden : hiddenNeurons) {
                    int j = hiddenNeurons.indexOf(hidden);
                    sum += hiddenWeights[j] * weightsHidden[j][k];
                }
                outputWeights[k] = Math.tanh(sum);
                actor.handle(results, output, outputWeights[k]);
            }
            return this;
        }
    }
}
