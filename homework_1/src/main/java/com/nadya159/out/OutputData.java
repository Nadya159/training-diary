package com.nadya159.out;

public interface OutputData {
    /**
     * Вывод данных
     *
     * @param message the data
     */
    void output(Object message);

    /**
     * Err output.
     *
     * @param message the data
     */
    void errorOutput(Object message);
}

