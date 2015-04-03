define(["three", "container"], function (THREE, container)
{
    function InformationPanel()
    {
        this.target = null;

        this.canvas = document.createElement('canvas');
        this.canvas.style.zIndex="1000";
        this.canvas.style.position = 'absolute';
        this.canvas.style.right = '-200px';
        this.canvas.style.top = '0px';
        this.canvas.width = 400;
        this.canvas.height = 900;
        container.appendChild(this.canvas);
    }

    InformationPanel.prototype.updateTarget = function updateTarget(codeUnitBar)
    {
        this.target = codeUnitBar;
    };

    InformationPanel.prototype.draw = function draw()
    {
        var context = this.canvas.getContext('2d');
        context.fillStyle="#ffffff";
        context.fillRect(0, 0, this.canvas.width, this.canvas.height);

        var x = 10;
        var y = 40;
        context.font = "20px Helvetica Neue";
        context.fillStyle = "#000000";
        context.fillText("Target Unit", x, y);

        context.font = "16px Helvetica Neue";
        context.fillText("Name: " + this.target.getName(), x, y + 30);
    };

    return new InformationPanel();
});