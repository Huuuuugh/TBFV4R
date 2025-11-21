package org.TBFV4R;

import org.TBFV4R.llm.Model;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        System.out.println(model.file2IFSF("dataset/ChangeLaneSimple.txt"));
    }
}
