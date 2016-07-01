//请不要修改本文件名
package serviceImpl;

import java.util.Arrays;
import java.util.Stack;

import service.ExecuteService;

public class ExecuteServiceImpl implements ExecuteService {
    String errorMessage = "";
    String output = "";

    private String code, input;
    private byte[] memory;
    private Stack<Integer> stack;
    private int memoryPtr, codePtr, inputPtr;

    public void init(String code, String input) {
        this.code = code;
        this.input = input;
        int maxSize = code.split(">").length;
        memory = new byte[maxSize];
        Arrays.fill(memory, (byte) 0);
        stack = new Stack<>();
        memoryPtr = 0;
        codePtr = 0;
        inputPtr = 0;
        output = "";
    }

    public void step() {
        switch (code.charAt(codePtr)) {
            case '>':
                memoryPtr++;
                break;
            case '<':
                if (memoryPtr > 0) memoryPtr--;
                break;
            case '+':
                memory[memoryPtr]++;
                break;
            case '-':
                memory[memoryPtr]--;
                break;
            case ',':
                if (inputPtr >= input.length()) {
                    memory[memoryPtr] = 0;
                } else {
                    memory[memoryPtr] = (byte) input.charAt(inputPtr);
                }
                inputPtr++;
                break;
            case '.':
                if (memory[memoryPtr] > 0) {
                    output = output + Character.toString((char) memory[memoryPtr]);
                }
                break;
            case '[':
                if (memory[memoryPtr] != 0) {
                    stack.push(codePtr);
                } else {
                    while (code.charAt(codePtr) != ']') {
                        codePtr++;
                    }
                }
                break;
            case ']':
                if (memory[memoryPtr] != 0) {
                    codePtr = stack.peek();
                } else {
                    stack.pop();
                }
                break;
        }
        codePtr++;
    }

    public void resume() {
        long startTime = System.currentTimeMillis();
        while (codePtr < code.length()) {
            step();
            if (System.currentTimeMillis() - startTime > 10000) {
                errorMessage = "Timeout";
                return;
            }
        }
    }

	/**
	 * 请实现该方法
	 */
	@Override
	public String execute(String code, String param) {
        this.init(code, param);
        this.resume();
        if (errorMessage.isEmpty()) {
            return output;
        } else {
            return errorMessage;
        }
	}
}
