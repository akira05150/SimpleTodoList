# SimpleTodoList

## 功能實現
1. 新增任務
    - 輸入標題
    - 輸入描述
    - 點擊日期可以更改日期(DatePicker)
    - 位置驗證，可按現在位置按鈕自動填入目前GPS定位，亦可手動輸入
    - 若標題或描述沒有填入，會有Toast提示

<img src="https://user-images.githubusercontent.com/70078018/155530446-ebbb6ff6-10de-41a0-8973-50631fa74c58.jpg" width="20%" height="20%" /> <img src="https://user-images.githubusercontent.com/70078018/155530419-2200842f-e72f-4575-a244-816f267e91bd.jpg" width="20%" height="20%" /> <img src="https://user-images.githubusercontent.com/70078018/155530430-cda306f3-74d8-42f6-b53a-c0029e0ba7c1.jpg" width="20%" height="20%" /> <img src="https://user-images.githubusercontent.com/70078018/155530439-d8152c44-3346-424f-901a-eea2e70713c0.jpg" width="20%" height="20%" />

2. 顯示任務列表
    - 依照截止日期，截止日期越近排序越前面(因不確定日期上的DSC是如何，我以一般todolist的想法，離截止日期越近的排越前面)

<img src="https://user-images.githubusercontent.com/70078018/155530492-b29c7df4-bdfd-490c-be83-f2184ee21dcc.jpg" width="20%" height="20%" />

3. 刪除任務
    - 按下垃圾桶按鍵即可刪除

<img src="https://user-images.githubusercontent.com/70078018/155530531-c02c8e8f-478a-4e02-943e-2792434756de.jpg" width="20%" height="20%" /> <img src="https://user-images.githubusercontent.com/70078018/155530537-0f2deade-c757-4fe7-9780-236e48a5ac81.jpg" width="20%" height="20%" />

4. 顯示每日一句
    - 連結cpp檔案取得每日一句內容，並存入DB中，換日即更新

## 開發時間
總計約17小時
  - 設計介面、實作介面呈現 4hr
  - 新增刪除等DB操作 7hr
  - 改善介面操作流暢性、美觀與Debug 3hr
  - Bonus 每日一句 3hr
