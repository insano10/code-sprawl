define(["three", "tween"], function (THREE, TWEEN)
{

    return  {

        lookAtFromAbove: function (positionObject, rotationObject, targetPosition, animationDuration)
        {
            new TWEEN.Tween(positionObject.position).to({
                x: targetPosition.x,
                y: targetPosition.y,
                z: targetPosition.z }, animationDuration)
                .easing(TWEEN.Easing.Quadratic.Out).start();

            new TWEEN.Tween(rotationObject.rotation).to({
                x: -0.5 * Math.PI,
                y: 0,
                z: 0 }, animationDuration)
                .easing(TWEEN.Easing.Quadratic.Out).start();
        }

    };
});
