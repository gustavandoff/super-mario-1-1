/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario;

/**
 *
 * @author gusta
 */
public class Collider {

    private boolean isCollidedX(int foreignX, int thisX, int foreignWidth, int thisWidth) {
        return (   foreignX                > thisX && foreignX                 < thisX + thisWidth // om vänsterkansten är inuti blocket
                || foreignX + foreignWidth > thisX && foreignX + foreignWidth  < thisX + thisWidth // om högerkanten är inuti blocket
                || foreignX               == thisX && foreignX + foreignWidth == thisX + thisWidth // om båda kanterna ligger rakt på blockets kanter
                || foreignX                < thisX && foreignX + foreignWidth  > thisX + thisWidth // om högerkanten är till höger om blocket och vänsterkanten är till vänster om blocket
                || foreignX                > thisX && foreignX + foreignWidth  < thisX + thisWidth // om båda kanterna är inuti blocket
                || foreignX               == thisX && foreignX + foreignWidth  > thisX + thisWidth // om foreigns ovankant ligger rakt på this ovankant och foreign underkant ligger utanför
                || foreignX               == thisX && foreignX + foreignWidth  < thisX + thisWidth // om foreigns ovankant ligger rakt på this ovankant och foreign underkant ligger innanför
                || foreignX                < thisX && foreignX + foreignWidth == thisX + thisWidth // om foreigns underkant ligger rakt på this underkant och foreign ovankant ligger utanför
                || foreignX                > thisX && foreignX + foreignWidth == thisX + thisWidth // om foreigns underkant ligger rakt på this underkant och foreign ovankant ligger innanför
                );
    }

    private boolean isCollidedY(int foreignY, int thisY, int foreignHeight, int thisHeight) {
        return (   foreignY                 > thisY && foreignY                  < thisY + thisHeight // om ovankanten är inuti blocket
                || foreignY + foreignHeight > thisY && foreignY + foreignHeight  < thisY + thisHeight // om underkantet är inuti blocket
                || foreignY                == thisY && foreignY + foreignHeight == thisY + thisHeight // om båda kanterna ligger rakt på blockets kanter
                || foreignY                 < thisY && foreignY + foreignHeight  > thisY + thisHeight // om ovankanten är över blocket och underkanten är under kanten
                || foreignY                 > thisY && foreignY + foreignHeight  < thisY + thisHeight // om båda kanterna är inuti blocket
                || foreignY                == thisY && foreignY + foreignHeight  > thisY + thisHeight // om foreigns ovankant ligger rakt på this ovankant och foreign underkant ligger utanför
                || foreignY                == thisY && foreignY + foreignHeight  < thisY + thisHeight // om foreigns ovankant ligger rakt på this ovankant och foreign underkant ligger innanför
                || foreignY                 < thisY && foreignY + foreignHeight == thisY + thisHeight // om foreigns underkant ligger rakt på this underkant och foreign ovankant ligger utanför
                || foreignY                 > thisY && foreignY + foreignHeight == thisY + thisHeight // om foreigns underkant ligger rakt på this underkant och foreign ovankant ligger innanför
                );
    }

    public boolean isCollided(int foreignX, int thisX, int foreignY, int thisY, int foreignWidth, int thisWidth, int foreignHeight, int thisHeight) {
        return isCollidedX(foreignX, thisX, foreignWidth, thisWidth) && isCollidedY(foreignY, thisY, foreignHeight, thisHeight);
    }
}
