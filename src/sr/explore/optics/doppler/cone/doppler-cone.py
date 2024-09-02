import ast
import matplotlib.pyplot as plt
import math
from matplotlib.patches import FancyArrowPatch

# Boosts along the +X axis.
# Projection of the Doppler cone onto the XY plane.
def show_data(data_file):
    thetas = []
    rs = []
    with open(data_file) as file:
        while line := file.readline():
            if not line.startswith("#"):
                # parse the string '[1.0, 2.2, 3.3, 4.5]' into a list of numbers
                v = ast.literal_eval(line.strip())
                ct = v[0]
                x = v[1]
                y = v[2]
                theta = math.atan2(y, x)
                r_squared = (x * x) + (y * y)
                r = math.sqrt(r_squared)
                thetas.append(theta)
                rs.append(r)
                color = 'g'
                if ct < 1.0:
                    color = 'r'
                elif ct > 1.0:
                    color = 'b'
                my_arrow = FancyArrowPatch(posA=(0, 0), posB=(theta, r), arrowstyle='->', color=color, mutation_scale=20, shrinkA=0, shrinkB=0)
                my_arrow.set_zorder(6)
                ax.add_artist(my_arrow)
    ax.set_rlabel_position(0)
    plt.yticks([1], ['1'])
    ax.plot(thetas, rs, color='grey', linestyle="-", linewidth=0.75)
    ax.scatter(0, 0, color='grey', s=100, alpha=0.90)

base_dir = "C:/johanley/ProjectsPhoton/special-relativity-core/src/sr/explore/optics/doppler/cone/"
data_file_Kp  = base_dir + "output_Kp.txt"
data_file_K  = base_dir + "output_K.txt"

fig, ax = plt.subplots(subplot_kw={'projection': 'polar'})
show_data(data_file_Kp)

# fig.suptitle("Projections of the Doppler cone onto the XY plane.\n At rest, and with a boost speed \u03B2 = +0.5 along the +X-axis.")
plt.show()

