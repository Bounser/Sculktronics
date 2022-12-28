package me.bounser.guitronics.components;

public enum EComponent {

    // BASIC COMPONENTS - 1 block width

    WIRE,
    DIODE,
    RESISTOR,
    // Transistor? It would have to be NPN
    // OpAmp

    // LOGIC GATES (Inverter = NOT)

    // If the logical gate is NAND, NOR & XNOR,
    // they are actually stored as their predecessor with one addition: an inverter module in the output.

    NOT,
    AND,
    OR,
    XOR

    // SPECIAL UTILITIES

    // Radio signal?

}
