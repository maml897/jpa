package uitls;

import java.util.function.Predicate;

public class Segment
{

	public double up;

	public double down;

	public Predicate<Double> predicate = x -> false;

	public Segment(double up,double down,Predicate<Double> predicate)
	{
		this.predicate = predicate;
	}

	public boolean test(double value)
	{
		return predicate.test(value);
	}
	
	public static void main(String[] args)
	{
		Segment segment =new Segment(10,20,x->{
			return x>10&x<20;
		});
		
		System.out.println(segment.test(25));
	}
}
