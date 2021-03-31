import os
import sys
import random

# FORMATO INPUT
#if 2D
#radio1 property    x1   y1   (alive/dead (0/1)) 
#radio1 property    x2   y2   (alive/dead (0/1)) 
#radio1 property    x3   y3   (alive/dead (0/1)) 

#if 23
#radio1 property1   x1   y1     z1   (alive/dead (0/1)) 
#radio1 property2   x2   y2     z2   (alive/dead (0/1)) 
#radio1 property3   x3   y3     z3   (alive/dead (0/1)) 


# Get the total number of args passed
total = len(sys.argv)
if total != 3:
    print("2 argument needed, 1. 2D or 3D, 2. length of the side of the matrix in wich there will be \'alive\'")
    quit()
if sys.argv[1] != "2D" and sys.argv[1] != "3D":
    print("2 argument needed, 1. 2D or 3D (check it has to be in CAPS_LOCK), 2.length of the side of the matrix in wich there will be \'alive\' ")
    quit()
input_file = "input" + ".txt"
if os.path.exists(input_file):
    os.remove(input_file)
input = open(input_file, "a")

OPEN_SPACE = 40
L = int(sys.argv[2])
w, h, d = L+ 2*OPEN_SPACE, L +2*OPEN_SPACE, L+2*OPEN_SPACE
if sys.argv[1] == "2D":
    for y in range(h):
        for x in range(w):
            # radius
            input.write("0.5")
            input.write('\t')
            # property
            input.write('1')
            input.write('\t')
            # x
            input.write(str(x))
            input.write('\t')
            # y
            input.write(str(y))
            input.write('\t')
            # alive/dead
            if y>OPEN_SPACE and y<h-OPEN_SPACE and x>OPEN_SPACE and x<w-OPEN_SPACE:
                input.write(str(random.choice([0,1])))
            else:
                input.write("0")
            input.write('\n')
else:
    for z in range(d):
        for y in range(h):
            for x in range(w):
                # radius
                input.write("1")
                input.write('\t')
                # property
                input.write('1')
                input.write('\t')
                # x
                input.write(str(x))
                input.write('\t')
                # y
                input.write(str(y))
                input.write('\t')
                # z 
                input.write(str(z))
                input.write('\t')
                # alive/dead
                if y>OPEN_SPACE and y<h-OPEN_SPACE and x>OPEN_SPACE and x<w-OPEN_SPACE and z>OPEN_SPACE and z<d-OPEN_SPACE:
                    input.write(str(random.choice([0,1])))
                else:
                    input.write("0")
                input.write('\n')

input.close()
print("the length of the matrix is of: " + str(w) )

