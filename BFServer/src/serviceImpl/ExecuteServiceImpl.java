//请不要修改本文件名
package serviceImpl;

import java.util.Arrays;
import java.util.Stack;

import service.ExecuteService;

public class ExecuteServiceImpl implements ExecuteService {
    String errorMessage = "";
	/**
	 * 请实现该方法
	 */
	@Override
	public String execute(String code, String param) {
        byte[] memory;
        int maxSize = code.split(">").length;
        memory = new byte[maxSize];
        Arrays.fill(memory, (byte) 0);

        Stack<Integer> stack = new Stack<>();
        int ptr = 0;
        int codePtr = 0;
        int inputPtr = 0;
        String output = "";

        long startTime = System.currentTimeMillis();

        while (codePtr < code.length()) {
            switch (code.charAt(codePtr)) {
                case '>':
                    ptr++;
                    break;
                case '<':
                    if (ptr > 0) ptr--;
                    break;
                case '+':
                    memory[ptr]++;
                    break;
                case '-':
                    memory[ptr]--;
                    break;
                case ',':
                    if (inputPtr >= param.length()) {
                        memory[ptr] = 0;
                    } else {
                        memory[ptr] = (byte) param.charAt(inputPtr);
                    }
                    inputPtr++;
                    break;
                case '.':
                    if (memory[ptr] > 0) {
                        output = output + Character.toString((char) memory[ptr]);
                    }
                    break;
                case '[':
                    if (memory[ptr] != 0) {
                        stack.push(codePtr);
                    } else {
                        while (code.charAt(codePtr) != ']') {
                            codePtr++;
                        }
                    }
                    break;
                case ']':
                    if (memory[ptr] != 0) {
                        codePtr = stack.peek();
                    } else {
                        stack.pop();
                    }
                    break;
            }
            codePtr++;
            if (System.currentTimeMillis() - startTime > 10000) {
                errorMessage = "Timeout";
                return null;
            }
        }
        return output;
	}
}
