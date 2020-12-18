# Calculator

I used [my A-Level computing project which is on GitHub](https://github.com/jamesw1892/Calculator) to answer today's challenge since it already implements all the logic, we just need to change the operator precedence.

I made the following adjustment to the `calculate` method of `Calc.py` to replace the default valid tokens with one passed to the function, but apart from that, the calculator is identical to the original. For this reason, I will not repeat the code here.

```python
def calculate(expr, tokens=None, debug=False):
    """
    Calculate the answer to 'expr'.
    If CalcError (or it's child CalcOperationError) has been raised,
    it is due to an invalid expression so needs to be caught and presented as an error message
    Any other exceptions are errors in the code

    :param expr (str): The expression to execute
    :param tokens (dict): Replacement valid tokens to use instead of the default
    :param debug (bool): Whether or not to print out extra information to check for errors. Default: False
    :return ans (str): The answer to 'expr'
    """

    assert isinstance(expr, str), "param 'expr' must be a string"

    # use replacement valid tokens if necessary
    global valid_tokens
    if tokens is not None:
        valid_tokens = tokens

    for func in [tokenise, convert, execute, post_calc]:

        # execute each function with the result from the last
        expr = func(expr)

        # output the progress if debug is on
        if debug:
            print(str(func).split("function ")[1].split(" at ")[0] + ":", repr(expr))

    return expr
```