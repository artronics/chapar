package it.unibo.sdwn.node;

public class LinkQuality implements Quality
{
    private int qualityValue;

    public LinkQuality(int qualityValue)
    {
        this.qualityValue = qualityValue;
    }

    public static int compare(int x, int y)
    {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    @Override
    public int getQualityValue()
    {
        return this.qualityValue;
    }

    @Override
    public void setQualityValue(int qualityValue)
    {
        this.qualityValue = qualityValue;
    }

    @Override
    public int compareTo(Quality anotherQuality)
    {
        return compare(this.qualityValue, anotherQuality.getQualityValue());
    }
}
