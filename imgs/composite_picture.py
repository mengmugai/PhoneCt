from PIL import Image
import os

# 设置目录和输出文件名
input_directory = os.path.dirname(os.path.abspath(__file__))
output_filename = "合并图.png"  # 修改为你的输出文件名

# 获取目录中所有PNG文件的文件列表
png_files = [f for f in os.listdir(input_directory) if f.endswith(".png")]

# 如果没有PNG文件，提醒并退出
if not png_files:
    print("目录中没有PNG文件。")
    exit()

# 以第一个PNG文件的尺寸为基准创建合并后的图像
base_image = Image.open(os.path.join(input_directory, png_files[0]))

# 计算合并后图像的总宽度和高度
total_width = base_image.width * len(png_files)
max_height = max([img.height for img in [base_image] + [Image.open(os.path.join(input_directory, f)) for f in png_files]])

# 创建一个新的合并后的图像
merged_image = Image.new("RGBA", (total_width, max_height))

# 将每个PNG文件粘贴到合并后的图像中
x_offset = 0
for png_file in png_files:
    img = Image.open(os.path.join(input_directory, png_file))
    merged_image.paste(img, (x_offset, 0))
    x_offset += img.width

# 保存合并后的图像
merged_image.save(output_filename)

print(f"合并完成，已保存为 {output_filename}")