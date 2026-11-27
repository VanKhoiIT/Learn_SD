# LAB 10 - Observer Design Pattern (Exercises 8.6 & 8.7)

## Cấu trúc dự án

- `src/main/java/designbook/solutions8/common/`:
  - `Day.java`: Enum 7 ngày trong tuần (`MONDAY` .. `SUNDAY`).
  - `Show.java`, `Movie.java`: Giao diện `Show` và lớp `Movie` (từ Chapter 6 / Exercise 6.13).
  - `IO.java`: Tiện ích xuất console chuẩn hóa.
- `src/main/java/designbook/solutions8/exercise06/` (**Exercise 8.6 - Push Data-Flow & Single Observer Interface**):
  - `ProgramObserver.java`: Giao diện Observer đơn gồm `showAdded(Show, Day)`, `showRemoved(Show, Day)`, `programCleared()`.
  - `ObservableProgram.java`: Lớp Subject đẩy trực tiếp `(Show, Day)` cho các `ProgramObserver`.
  - `ConcreteProgramObserver.java`: Lớp ConcreteObserver dùng để kiểm thử.
  - `Exercise06.java`: Chương trình minh họa Exercise 8.6.
- `src/main/java/designbook/solutions8/exercise07/` (**Exercise 8.7 - Pull Data-Flow & Segregated Interfaces with ISP**):
  - `ProgramAdditionObserver.java`: Giao diện Observer riêng cho sự kiện thêm (`showAdded(Iterable<Show>)`).
  - `ProgramRemovalObserver.java`: Giao diện Observer riêng cho sự kiện xóa và làm rỗng (`showRemoved(Iterable<Show>)`, `programCleared()`).
  - `ObservableProgram.java`: Thực thi `Iterable<Show>` (duyệt qua 7 ngày trong tuần theo thứ tự cố định) để đảm bảo **Interface Segregation Principle (ISP)** — Observer chỉ nhận góc nhìn `Iterable<Show>` nên không thể gọi các hàm làm thay đổi trạng thái Subject (`add`, `remove`, `clear`).
  - `ConcreteObserver1.java`, `ConcreteObserver2.java`, `ConcreteObserver3.java`: 3 lớp ConcreteObserver minh họa đăng ký từng phần hoặc kết hợp cả hai giao diện.
  - `Exercise07.java`: Chương trình minh họa Exercise 8.7.

## Cách biên dịch và chạy

Chạy trực tiếp file `build_and_run.bat`:
```bat
.\build_and_run.bat
```

