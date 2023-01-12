package me.bounser.sculktronics.components;

public enum EComponent {

    // BASIC COMPONENTS - 1 block width

    WIRE,
    DIODE,
    RESISTOR,
    // Transistor? It would have to be NPN or (More probably MOSFET)
    // OpAmp
    // Multiplexer ?
    // Binary to decimal
    // Display (LED with 7 segments ¿?)
    //

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
