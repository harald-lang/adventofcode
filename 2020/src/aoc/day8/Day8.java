package aoc.day8;

import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import lombok.val;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.BitSet;

import static java.lang.System.out;

public class Day8 {

    @Value
    @ToString
    private static class Instruction {

        String operation;
        int argument;

        public static Instruction parse(String line) {
            val split = line.split(" ");
            val op = split[0];
            val arg = Integer.parseInt(split[1]);
            return new Instruction(op, arg);
        }

    }

    private static class Interpreter {

        @Getter
        private int ip;
        @Getter
        private int acc;
        @Getter
        private int status;

        private final ArrayList<Instruction> code;
        private final BitSet exec;

        public Interpreter(ArrayList<Instruction> code) {
            reset();
            this.code = code;
            this.exec = new BitSet(code.size());
        }

        public void reset() {
            this.ip = 0;
            this.acc = 0;
            this.status = -1; // not yet started
        }

        public int step() {
            if (ip == code.size()) {
                status = 1; // terminated successfully
                return status;
            }

            if (ip > code.size()) {
                status = 3; // instruction pointer out of bound
                return status;
            }

            if (exec.get(ip)) {
                status = 2; // cycle detected
                return status;
            }

            exec.set(ip); // mark instruction as executed

            val instr = code.get(ip);
            switch (instr.getOperation()) {
                case "nop" -> ip++;
                case "acc" -> {
                    acc += instr.getArgument();
                    ip++;
                }
                case "jmp" -> ip += instr.getArgument();
            }

            status = 0;
            return status;
        }

        public int run() {
            var status = -1;
            while ((status = step()) == 0) {}
            return status;
        }

    }

    public static void main(String[] args) throws Exception {
        val inputPath = Path.of(Day8.class.getResource("input.txt").getPath());
        val inputs =  Files.readAllLines(inputPath);

        val code = new ArrayList<Instruction>();
        inputs.forEach(line -> {
            val instr = Instruction.parse(line);
            code.add(instr);
        });

        {
            val interpreter = new Interpreter(code);
            interpreter.run();
            out.println("part 1: " + interpreter.getAcc());
        }

        {
            // Brute force.
            for (int i = 0; i < code.size(); ++i) {
                val orgInstr = code.get(i);
                if (orgInstr.getOperation().equals("acc")) {
                    continue;
                }
                // mutate the code
                val mutCode = new ArrayList<>(code);
                if (orgInstr.getOperation().equals("nop")) {
                    mutCode.set(i, new Instruction("jmp", orgInstr.getArgument()));
                } else if (orgInstr.getOperation().equals("jmp")) {
                    mutCode.set(i, new Instruction("nop", orgInstr.getArgument()));
                }

                // execute the mutated code
                val interpreter = new Interpreter(mutCode);
                val status = interpreter.run();
                if (status == 1) {
                    out.println("part 2: " + interpreter.getAcc());
                    break;
                }

            }
        }
    }
}
