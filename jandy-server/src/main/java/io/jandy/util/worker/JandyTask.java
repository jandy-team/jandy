package io.jandy.util.worker;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class JandyTask implements Serializable {
    public static final int UPDATE = 0, SAVE = 1, FINISH = 2;

    private int type;
    private Serializable data;
}