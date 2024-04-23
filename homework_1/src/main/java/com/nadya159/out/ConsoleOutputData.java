package com.nadya159.out;

public class ConsoleOutputData implements OutputData{
    @Override
    public void output(Object data) {
        System.out.println(data.toString());
    }

    @Override
    public void errorOutput(Object data) {
        System.err.println(data.toString());
    }
}
