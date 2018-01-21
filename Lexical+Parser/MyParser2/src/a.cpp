#include <iostream>
#include <stdlib.h>
using namespace std;
void f(int x, int y)
{
if(y-x)
{
cout<<x;
}
else 
{
f(x,y+1);
 
}


}
double add(double x, double y)
{
return x+y;

}

int main()
{
	double a=1.2;
	double b=a*3*4;
	cout<<add(a,b);
	f(2,0);
	system("pause");
 } 
 

