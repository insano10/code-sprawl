define(["three", "container"], function (THREE, container)
{
    //static
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

    InformationPanel.prototype.updateTarget = function updateTarget(fileUnitBar)
    {
        this.target = fileUnitBar;
    };

    InformationPanel.prototype.clearTarget = function clearTarget()
    {
        this.target = null;
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
        context.fillText("Unit Inspection", x, y);

        if(this.target != null)
        {
            context.font = "16px Helvetica Neue";
            context.fillText("Neighbourhood: " + this.target.getGroupName(), x, y + 30);
            context.fillText("Unit Name: " + this.target.getName(), x, y + 60);
            context.fillText("File Extension: " + this.target.getFileExtension(), x, y + 90);
            context.fillText("Line count: " + this.target.getLineCount(), x, y + 120);

            if(this.target.getVcsInfo() != null)
            {
                var vcsInfo = this.target.getVcsInfo();

                var lastModifiedDate = new Date(vcsInfo.lastModifiedTimeMillis);

                context.fillText("Last Modified Time: " + lastModifiedDate.toLocaleString(), x, y + 180);
                context.fillText("Last Modified by User: " + vcsInfo.lastModifiedByUser, x, y + 210);
            }
            else
            {
                context.fillText("No VCS history found", x, y + 180);
            }
        }
    };

    return new InformationPanel();
});