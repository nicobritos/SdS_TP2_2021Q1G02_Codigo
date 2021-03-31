import os
import sys
import random
import math

# FORMATO INPUT
#if 2D
#M
#radio1 property    x1   y1   (alive/dead (0/1))
#radio1 property    x2   y2   (alive/dead (0/1)) 
#radio1 property    x3   y3   (alive/dead (0/1)) 

#if 3D
#M
#radio1 property1   x1   y1     z1   (alive/dead (0/1))
#radio1 property2   x2   y2     z2   (alive/dead (0/1)) 
#radio1 property3   x3   y3     z3   (alive/dead (0/1)) 


# Get the total number of args passed
total = len(sys.argv)
if total != 4:
    print("3 argument needed, 1. 2D or 3D, 2. length of the side of the matrix in which there will be \'alive\', 3. percentage of alive particles (float)")
    quit()
if sys.argv[1] != "2D" and sys.argv[1] != "3D":
    print("3 argument needed, 1. 2D or 3D (check it has to be in CAPS_LOCK), 2.length of the side of the matrix in which there will be \'alive\' (L^2 (or L^3) = No of cells), 3. percentage of alive particles (float)")
    quit()
input_file = "input" + ".txt"
if os.path.exists(input_file):
    os.remove(input_file)
input = open(input_file, "a")

OPEN_SPACE = 40
L = int(sys.argv[2])
w, h, d = L+ 2*OPEN_SPACE, L +2*OPEN_SPACE, L+2*OPEN_SPACE
alive_percentage = float(sys.argv[3]) / 100

input.write(str(w))
input.write('\n')

if sys.argv[1] == "2D":
    alive_count = math.ceil(L ** 2 * alive_percentage)
    dead_count = math.floor(L ** 2 * (1 - alive_percentage))

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
                if alive_count > 0 and dead_count > 0:
                    alive = random.choice([0, 1])

                    if alive == 1:
                        alive_count -= 1
                    else:
                        dead_count -= 1

                    input.write(str(alive))
                elif alive_count > 0:
                    alive_count -= 1
                    input.write('1')
                else:
                    dead_count -= 1
                    input.write('0')
            else:
                input.write("0")
            input.write('\n')
else:
    alive_count = math.ceil(L ** 3 * alive_percentage)
    dead_count = math.floor(L ** 3 * (1 - alive_percentage))

    for z in range(d):
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
                # z 
                input.write(str(z))
                input.write('\t')
                # alive/dead
                if y>OPEN_SPACE and y<h-OPEN_SPACE and x>OPEN_SPACE and x<w-OPEN_SPACE and z>OPEN_SPACE and z<d-OPEN_SPACE:
                    if alive_count > 0 and dead_count > 0:
                        alive = random.choice([0, 1])

                        if alive == 1:
                            alive_count -= 1
                        else:
                            dead_count -= 1

                        input.write(str(alive))
                    elif alive_count > 0:
                        alive_count -= 1
                        input.write('1')
                    else:
                        dead_count -= 1
                        input.write('0')
                else:
                    input.write("0")
                input.write('\n')

input.close()
