package com.example.studiograficzne;

public class Plant {

        private String id;
        private int level;
        private int price;

        public Plant(String id, int level, int price) {
            this.id = id;
            this.level = level;
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

}
