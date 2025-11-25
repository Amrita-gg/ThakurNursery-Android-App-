package com.example.thakur_nursery;

import java.util.List;

public class PlantResponse {
    private Result result;

    public Result getResult() {
        return result;
    }

    public static class Result {
        private Classification classification;

        public Classification getClassification() {
            return classification;
        }

        public static class Classification {
            private List<Suggestion> suggestions;

            public List<Suggestion> getSuggestions() {
                return suggestions;
            }

            public static class Suggestion {
                private String name;
                private Details details;

                public String getName() {
                    return name;
                }

                public Details getDetails() {
                    return details;
                }

                public static class Details {
                    private List<String> common_names;
                    private Description description;

                    public List<String> getCommonNames() {
                        return common_names;
                    }

                    public Description getDescription() {
                        return description;
                    }

                    public static class Description {
                        private String value;

                        public String getValue() {
                            return value;
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "PlantResponse{" +
                "result=" + result +
                '}';
    }
}
