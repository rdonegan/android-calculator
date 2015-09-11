package edu.cmu.rdonegan.simplecalculator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements OnClickListener,OnLongClickListener  {
	TextView output;
	private double firstNum, secondNum, memory, result = 0; //holds the value of the first number
	private double tempResult = 0; //value used to track use of MC functions
    private int function = -1; //identifies operation to perform
    private String functionSym = "";
    private boolean isEmpty=true;
    Button clearBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output=(TextView)findViewById(R.id.output);
        clearBt = (Button)findViewById(R.id.clearButton);
        clearBt.setOnClickListener(this);
        clearBt.setOnLongClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu); 
        return true;
    }
    
    //appends number to screen if clicked
    public void num_Clicked(View sender){
    	//if the function has already been selected, then this is the
    	//the second number being entered and the display should clear.
    	if(function != -1 && isEmpty==false){
    		output.setText("");
    		isEmpty=true;
    	}
    	else if(isEmpty==false){
    		output.setText("");
    		isEmpty = true;
    	}
    	Button bt=(Button)sender;
    	output.append(bt.getText());
    			
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    //activated after user selects valid operator.
    //sets variable function value.
    public void setFunction(View sender){
    	//checks to see if function has already been entered.
    	//if already entered, does nothing.
    	if (function != -1){
    		return;
    	}
    	//open toast if operator used out of order
    	else if(output.getText().toString().equals("") || output.getText().toString().indexOf("=")!= -1){ 
    		toastMessage(sender);
    		onLongClick(sender);
    		return;
    	}
    	
    	Button bt = (Button)sender;
    	//determine what number to set function to
    	if(bt.getText().toString().equals("+")){
    		function = 0;
    	}
    	else if (bt.getText().toString().equals("-")){
    		function = 1;
    	}
    	else if(bt.getText().toString().equals("*")){
    		function = 2;
    	}
    	else if(bt.getText().toString().equals("/")){
    		function = 3;
    	}
    	
    	//get value of number in display and set to firstNum
    	firstNum = Double.parseDouble(output.getText().toString());
    	
    	//add operand to function
    	functionSym = bt.getText().toString();
    	output.append(" " + bt.getText());
    	isEmpty=false;
    	
    }
    
    //returns true if equations is in number operator number format
    //false, otherwise
    public boolean isValidEquation(View sender){
    	String currDisp = output.getText().toString();
    	if(!currDisp.equals("") && !functionSym.equals("") && currDisp.indexOf(" ")==-1){
    		return true;
    	}
    	else{
    		return false;
    	}
    	
    	
    }
    
    public void performFunction(View sender){
    	//checks to make sure equal hasn't been pressed out of order
    	if(!isValidEquation(sender)){ 
    		toastMessage(sender);

    		return;
    	}
    	//get the second num
    	secondNum = Double.parseDouble(output.getText().toString());
    	//iterate through to correct function
    	if(function==0){
    		result = firstNum + secondNum;
    	}
    	else if (function==1){
    		result = firstNum - secondNum;
    	}
    	else if (function==2){
    		result = firstNum * secondNum;
    	}
    	else if (function==3){
    		result = firstNum / secondNum;
    	}
    	
    	output.setText(firstNum + " " + functionSym + " " + secondNum + " = " + result);
    	
    	//set tempResult=result before resetting result so that MC can store this value
    	tempResult=result;
    	
    	//reset variables
    	firstNum = 0;
    	secondNum = 0;
    	result = 0;
    	function=-1;
    	functionSym="";
    	isEmpty=false;
    }
    
    //add decimal to number being entered
    public void addDecimal(View sender){
    	String display = output.getText().toString();

    	//if display includes a symbol other than an integer, return
    	if(isEquationSymbol(sender, display)){
    		return;
    	}
    	//if decimal is first item being added, append 0
    	else if(output.getText().toString().equals("")){
    		output.setText("0.");
    		return;
    	}

    	double currNum = Double.parseDouble(output.getText().toString());
    	//if there's already a decimal, return
    	if(output.getText().toString().indexOf(".")!=-1){
    		return;
    	}
    	else{
    		output.append(".");
    	}
    }
    
    public boolean isEquationSymbol(View sender, String str){
    	String[] operators = {"+", "-", "*", "/"};
    	for(int i=0; i<operators.length; i++){
    		if (str.contains(operators[i])){
    			return true;
    		}
    	}
    	return false;
    }
    
    //commit number currently on screen to memory
    public void changeMemory(View sender){
    	//if display is empty, return and commit nothing to memory
    	if(output.getText().toString().equals("")){
    		return;
    	}
    	
    	Button bt=(Button)sender;
    	String command = bt.getText().toString();
    	String displayText = output.getText().toString();
    	if(command.equals("M+")){ //add value to memory
    		if(displayText.indexOf("+") != -1 && displayText.indexOf("=") == -1){   //use this case if the output is showing full equation
    			return;
    		}
    		else if(displayText.indexOf("=")!=-1){ //if a full equation, add tempResult to memory then reset
    			memory += tempResult;
    			tempResult = 0;	
    		}
    		else{
    			memory += Double.parseDouble(displayText);
    		}
    		

    	}
    	else{ //subtract value from memory
    		if(displayText.indexOf("+") != -1 && displayText.indexOf("=") == -1){   //use this case if the output is showing full equation
    			return;
    		}
    		else if(displayText.indexOf("=")!=-1){
    			memory -= tempResult;
    			tempResult = 0;
    			
    		}
    		else{
    			memory -= Double.parseDouble(displayText);
    		}
    		
    	}
    }
    
    //show the current memory value
    public void showMemory(View sender){
    	output.setText(memory+"");
    }
    
    //reset memory value to 0
    public void clearMemory(View sender){
    	memory = 0;
    }
    
    //displayed if an operator or equal sign is used out of order
    public void toastMessage(View v){
    	Toast warning = Toast.makeText(getApplicationContext(), "Entries must be in 'num operator num' format.", Toast.LENGTH_SHORT);
    	warning.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, -250);
    	warning.show();
    }


	@Override
	//if 'C' button pressed and held, reset all variables
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		//reset variables
    	firstNum = 0;
    	secondNum = 0;
    	result = 0;
    	function=-1;
    	functionSym="";
    	isEmpty=true;
    	output.setText("");
		return true;
	}


	@Override
	//delete the last character on screen, if any
	public void onClick(View v) {
		//original output string
		String currDisp = output.getText().toString();
		//if empty, do nothing
		if (currDisp.equals("")){
			return;
		}
		else{ //remove last character
			String newDisp = currDisp.substring(0,currDisp.length()-1);
			output.setText(newDisp);
		}
		
	}
}
