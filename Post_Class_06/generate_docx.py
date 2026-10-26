# -*- coding: utf-8 -*-
"""
Script to generate the perfected, rigorous Master Word report:
Incorporating all 5 user feedback points:
1. Chốt dứt khoát đáp án Bài 10 là 3 lần thực thi (chuyển 2 thành ghi chú phụ).
2. Mô tả chính xác TestRunner (custom Reflection runner, không có launcher).
3. Ghi rõ môi trường Java 25 biên dịch với --release 21 (bytecode v65) và dán log thật.
4. Làm rõ câu chữ về việc không sửa thuật toán UUT nhưng có chỉnh nhẹ lớp PokerHand (cards, getCards, assert pCards != null) và giải thích e3 vs designbook.
5. Sửa câu ở mục 2.6 về 40 sảnh true theo luật Poker và 2.59M sảnh false.
"""

import os
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml import parse_xml
from docx.oxml.ns import nsdecls

def set_cell_background(cell, fill_hex):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    tcPr.append(shd)

def set_cell_margins(cell, top=100, bottom=100, left=150, right=150):
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = parse_xml(f'<w:tcMar {nsdecls("w")}><w:top w:w="{top}" w:type="dxa"/><w:bottom w:w="{bottom}" w:type="dxa"/><w:left w:w="{left}" w:type="dxa"/><w:right w:w="{right}" w:type="dxa"/></w:tcMar>')
    tcPr.append(tcMar)

def add_chapter_heading(doc, text):
    h = doc.add_heading(text, level=1)
    run = h.runs[0]
    run.font.name = 'Times New Roman'
    run.font.size = Pt(14)
    run.font.bold = True
    run.font.color.rgb = RGBColor(30, 58, 138) # Dark Navy Blue
    h.paragraph_format.space_before = Pt(18)
    h.paragraph_format.space_after = Pt(6)
    return h

def add_section_heading(doc, text):
    h = doc.add_heading(text, level=2)
    run = h.runs[0]
    run.font.name = 'Times New Roman'
    run.font.size = Pt(12.5)
    run.font.bold = True
    run.font.color.rgb = RGBColor(29, 78, 216) # Primary Blue
    h.paragraph_format.space_before = Pt(12)
    h.paragraph_format.space_after = Pt(4)
    return h

def add_subsection_heading(doc, text):
    h = doc.add_heading(text, level=3)
    run = h.runs[0]
    run.font.name = 'Times New Roman'
    run.font.size = Pt(11.5)
    run.font.bold = True
    run.font.italic = True
    run.font.color.rgb = RGBColor(15, 23, 42) # Slate Dark
    h.paragraph_format.space_before = Pt(8)
    h.paragraph_format.space_after = Pt(3)
    return h

def add_paragraph(doc, text, bold_prefix=None, space_after=4):
    p = doc.add_paragraph()
    p.paragraph_format.space_before = Pt(0)
    p.paragraph_format.space_after = Pt(space_after)
    p.paragraph_format.line_spacing = 1.15
    if bold_prefix:
        r_pre = p.add_run(bold_prefix)
        r_pre.font.name = 'Times New Roman'
        r_pre.font.size = Pt(11.5)
        r_pre.font.bold = True
        r_pre.font.color.rgb = RGBColor(15, 23, 42)
    r = p.add_run(text)
    r.font.name = 'Times New Roman'
    r.font.size = Pt(11.5)
    return p

def add_bullet(doc, text, bold_prefix=None):
    p = doc.add_paragraph(style='List Bullet')
    p.paragraph_format.space_before = Pt(0)
    p.paragraph_format.space_after = Pt(3)
    p.paragraph_format.line_spacing = 1.15
    if bold_prefix:
        r_pre = p.add_run(bold_prefix)
        r_pre.font.name = 'Times New Roman'
        r_pre.font.size = Pt(11.5)
        r_pre.font.bold = True
    r = p.add_run(text)
    r.font.name = 'Times New Roman'
    r.font.size = Pt(11.5)
    return p

def add_code_box(doc, code_text):
    tbl = doc.add_table(rows=1, cols=1)
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    cell = tbl.cell(0, 0)
    set_cell_background(cell, "F8FAFC")
    set_cell_margins(cell, top=100, bottom=100, left=150, right=150)
    
    tcPr = cell._tc.get_or_add_tcPr()
    borders = parse_xml(f'<w:tcBorders {nsdecls("w")}><w:left w:val="single" w:sz="24" w:space="0" w:color="2563EB"/><w:top w:val="single" w:sz="4" w:space="0" w:color="CBD5E1"/><w:right w:val="single" w:sz="4" w:space="0" w:color="CBD5E1"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="CBD5E1"/></w:tcBorders>')
    tcPr.append(borders)
    
    p = cell.paragraphs[0]
    p.paragraph_format.space_before = Pt(2)
    p.paragraph_format.space_after = Pt(2)
    p.paragraph_format.line_spacing = 1.15
    run = p.add_run(code_text.strip())
    run.font.name = 'Consolas'
    run.font.size = Pt(9.5)
    
    doc.add_paragraph().paragraph_format.space_after = Pt(4)

def add_callout_box(doc, title, text_content, box_type="info"):
    tbl = doc.add_table(rows=1, cols=1)
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    cell = tbl.cell(0, 0)
    
    bg_color = "EFF6FF" if box_type == "info" else "FEF3C7"
    border_color = "3B82F6" if box_type == "info" else "F59E0B"
    title_color = RGBColor(30, 58, 138) if box_type == "info" else RGBColor(180, 83, 9)
    icon = "📌" if box_type == "info" else "⚠️"
    
    set_cell_background(cell, bg_color)
    set_cell_margins(cell, top=110, bottom=110, left=150, right=150)
    
    tcPr = cell._tc.get_or_add_tcPr()
    borders = parse_xml(f'<w:tcBorders {nsdecls("w")}><w:left w:val="single" w:sz="30" w:space="0" w:color="{border_color}"/><w:top w:val="none"/><w:right w:val="none"/><w:bottom w:val="none"/></w:tcBorders>')
    tcPr.append(borders)
    
    p = cell.paragraphs[0]
    p.paragraph_format.space_before = Pt(2)
    p.paragraph_format.space_after = Pt(2)
    p.paragraph_format.line_spacing = 1.15
    
    r_title = p.add_run(f"{icon} {title}: ")
    r_title.font.name = 'Times New Roman'
    r_title.font.size = Pt(11)
    r_title.font.bold = True
    r_title.font.color.rgb = title_color
    
    r_text = p.add_run(text_content)
    r_text.font.name = 'Times New Roman'
    r_text.font.size = Pt(11)
    r_text.font.italic = True
    
    doc.add_paragraph().paragraph_format.space_after = Pt(4)

def add_table_data(doc, headers, rows_data):
    tbl = doc.add_table(rows=len(rows_data) + 1, cols=len(headers))
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    
    for i, h_text in enumerate(headers):
        c = tbl.cell(0, i)
        set_cell_background(c, "1E3A8A")
        set_cell_margins(c, 80, 80, 100, 100)
        p = c.paragraphs[0]
        r = p.add_run(h_text)
        r.font.name = 'Times New Roman'
        r.font.size = Pt(10.5)
        r.font.bold = True
        r.font.color.rgb = RGBColor(255, 255, 255)
        
    for r_idx, row in enumerate(rows_data, start=1):
        bg = "FFFFFF" if r_idx % 2 != 0 else "F8FAFC"
        for c_idx, val in enumerate(row):
            cell = tbl.cell(r_idx, c_idx)
            set_cell_background(cell, bg)
            set_cell_margins(cell, 70, 70, 90, 90)
            p = cell.paragraphs[0]
            r = p.add_run(val)
            r.font.name = 'Times New Roman'
            r.font.size = Pt(10)
            
    doc.add_paragraph().paragraph_format.space_after = Pt(6)

def build_perfect_report():
    doc = docx.Document()
    
    # Page setup
    for s in doc.sections:
        s.top_margin = Inches(0.8)
        s.bottom_margin = Inches(0.8)
        s.left_margin = Inches(0.9)
        s.right_margin = Inches(0.9)
        
    # Header Banner
    header_tbl = doc.add_table(rows=2, cols=1)
    header_tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    c0 = header_tbl.cell(0, 0)
    set_cell_background(c0, "1E3A8A")
    set_cell_margins(c0, top=140, bottom=100, left=200, right=200)
    p0 = c0.paragraphs[0]
    p0.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r0 = p0.add_run("BÁO CÁO BÀI TẬP KIỂM THỬ PHẦN MỀM CHUYÊN SÂU\nChủ đề: Kiểm thử đơn vị lớp PokerHand trong Java\nNội dung: Exercise 5.8, Exercise 5.9 và Exercise 5.10")
    r0.font.name = 'Times New Roman'
    r0.font.size = Pt(13)
    r0.font.bold = True
    r0.font.color.rgb = RGBColor(255, 255, 255)
    
    c1 = header_tbl.cell(1, 0)
    set_cell_background(c1, "F1F5F9")
    set_cell_margins(c1, top=100, bottom=100, left=200, right=200)
    p1 = c1.paragraphs[0]
    p1.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r1 = p1.add_run("Học phần: Thiết kế Phần mềm (Software Design) | Môi trường: JDK 25 (target Java 21 LTS) | JUnit 5.10.2\nGiáo trình: Introduction to Software Design with Java (3rd Edition - Martin P. Robillard)\nDự án: Post_Class_06 | Phiên bản hiệu chỉnh hoàn thiện & chuẩn hóa học thuật")
    r1.font.name = 'Times New Roman'
    r1.font.size = Pt(10.5)
    r1.font.italic = True
    r1.font.color.rgb = RGBColor(30, 58, 138)
    
    doc.add_paragraph().paragraph_format.space_after = Pt(10)

    # =========================================================================
    # CHƯƠNG 1
    # =========================================================================
    add_chapter_heading(doc, "CHƯƠNG 1. GIỚI THIỆU BÀI TẬP VÀ PHẠM VI NGHIÊN CỨU")
    
    add_section_heading(doc, "1.1. Mục đích và Yêu cầu kiểm thử")
    add_paragraph(doc, "Bài tập nhằm tìm hiểu và áp dụng các kỹ thuật kiểm thử phần mềm chuyên nghiệp thông qua việc xây dựng bộ kiểm thử đơn vị (Unit Testing) toàn diện cho lớp PokerHand trong ngôn ngữ Java.")
    add_paragraph(doc, "Các mục tiêu cụ thể được giải quyết bao gồm:")
    add_bullet(doc, "Tìm hiểu cách kiểm thử phương thức có phạm vi truy cập private thông qua Java Reflection API mà không cần sửa đổi phạm vi truy cập của mã nguồn sản phẩm.")
    add_bullet(doc, "Xây dựng các trường hợp kiểm thử chức năng (Functional Test Cases) cho kết quả true và false.")
    add_bullet(doc, "Phân tích không gian dữ liệu đầu vào và chứng minh giới hạn của kiểm thử vét cạn (Exhaustive Testing).")
    add_bullet(doc, "Áp dụng kỹ thuật Statement Coverage (Độ bao phủ câu lệnh) và xác định số lần thực thi tối thiểu của đối tượng kiểm thử (UUT): Tối thiểu đúng 2 lần gọi.")
    add_bullet(doc, "Áp dụng kỹ thuật Branch Coverage (Độ bao phủ nhánh) và phân tích cơ chế đánh giá ngắn mạch (Short-circuit Evaluation) của toán tử logic OR (||): Chốt đáp án tối thiểu đúng 3 lần gọi.")
    add_bullet(doc, "Phân biệt rành mạch giữa số lượng phương thức @Test trong JUnit 5 và số lần kích hoạt thực tế phương thức UUT.")

    add_section_heading(doc, "1.2. Giới thiệu lớp PokerHand và phương thức isStraightFlush()")
    add_paragraph(doc, "Lớp PokerHand biểu diễn một tay bài gồm 5 lá bài Poker (được phát triển như một phần giải pháp cho bài tập 3.12 trong giáo trình). Các lá bài được lưu trữ trong danh sách aCards và tự động được sắp xếp theo thứ tự tăng dần của thứ bậc (Rank) ngay khi khởi tạo.")
    add_paragraph(doc, "Phương thức private isStraightFlush() có nhiệm vụ kiểm tra xem 5 lá bài có tạo thành một bộ Thùng phá sảnh (Straight Flush) hay không. Về mặt quy tắc Poker tiêu chuẩn:")
    add_bullet(doc, "Cả 5 lá bài đều phải có cùng một chất (Suit).")
    add_bullet(doc, "Thứ bậc của 5 lá bài phải liên tiếp nhau (Rank) với hiệu số thứ tự bậc kề nhau bằng đúng 1.")

    add_section_heading(doc, "1.3. Phạm vi áp dụng, đối tượng UUT và cấu trúc Package e3.chapter5")
    add_paragraph(doc, "Đối tượng kiểm thử trực tiếp (UUT - Unit Under Test) trong báo cáo này là phương thức private isStraightFlush() thuộc lớp e3.chapter5.PokerHand.")
    add_paragraph(doc, "Giải thích về cấu trúc Package e3.chapter5 và phương thức truy xuất:", bold_prefix="Lý giải học thuật: ")
    add_bullet(doc, "Trong đề bài ban đầu, đoạn mã mẫu được trích dẫn theo phong cách Java Beans truyền thống (pCard1.getRank(), getSuit()) thuộc phiên bản cũ (2nd Edition, package designbook.chapter5).")
    add_bullet(doc, "Tuy nhiên, trên kho mã nguồn GitHub chính thức của tác giả Martin P. Robillard (repo prmr/DesignBook), toàn bộ mã nguồn bài tập đã được nâng cấp lên ấn bản mới nhất (3rd Edition - 2025, package e3.chapter5). Ở phiên bản này, lớp Card và các enum Rank, Suit được thiết kế theo phong cách hiện đại (tương tự Java Record) với các accessor ngắn gọn là rank() và suit().")
    add_bullet(doc, "Do đó, dự án nộp được chuẩn hóa đồng bộ theo cấu trúc package e3.chapter5 của giáo trình Edition 3 mới nhất, bảo đảm tính hiện đại và tương thích hoàn toàn với mã nguồn chuẩn của tác giả.")

    # =========================================================================
    # CHƯƠNG 2
    # =========================================================================
    add_chapter_heading(doc, "CHƯƠNG 2. EXERCISE 5.8 – KIỂM THỬ PHƯƠNG THỨC PRIVATE VÀ KHÔNG GIAN MẪU")
    
    add_section_heading(doc, "2.1. Yêu cầu bài toán")
    add_paragraph(doc, "Đề bài yêu cầu: Viết lớp kiểm thử TestPokerHand với các phương thức trợ giúp cho phép kiểm thử phương thức private isStraightFlush(), sau đó viết ít nhất một ca kiểm thử có kết quả trả về true. Đồng thời giải quyết 2 câu hỏi lý thuyết cốt lõi:")
    add_bullet(doc, "Cần bao nhiêu lần thực thi UUT để kiểm thử tất cả các bộ Straight Flush có thể có?")
    add_bullet(doc, "Việc kiểm thử tất cả các bộ Straight Flush có đồng nghĩa với việc đạt kiểm thử vét cạn (exhaustive test coverage) hay không?")

    add_section_heading(doc, "2.2. Kỹ thuật kiểm thử phương thức private qua Java Reflection API")
    add_paragraph(doc, "Trong thiết kế hướng đối tượng, phương thức private bị đóng gói chặt chẽ và không thể gọi trực tiếp từ bên ngoài lớp. Để kiểm thử hộp trắng mà không làm phá vỡ nguyên lý đóng gói (không đổi phạm vi truy cập sang public hay package-private), chúng ta sử dụng Java Reflection API thông qua phương thức invokeIsStraightFlush():")
    
    code_reflection = """public static boolean invokeIsStraightFlush(PokerHand pHand) {
    try {
        Method method = PokerHand.class.getDeclaredMethod("isStraightFlush");
        method.setAccessible(true);
        return (boolean) method.invoke(pHand);
    } catch (ReflectiveOperationException e) {
        fail("Reflection failed to invoke private isStraightFlush: " + e.getMessage());
        return false;
    }
}"""
    add_code_box(doc, code_reflection)

    add_section_heading(doc, "2.3. Thiết kế trường hợp kiểm thử chức năng")
    add_paragraph(doc, "Bảng thiết kế các ca kiểm thử chức năng cho isStraightFlush():")
    
    headers_tc = ["Mã Test", "Mô tả ca kiểm thử", "Dữ liệu 5 lá bài đầu vào", "Kết quả mong đợi"]
    data_tc = [
        ["TC01", "5 lá liên tiếp, cùng chất (Straight Flush chuẩn)", "2♣, 3♣, 4♣, 5♣, 6♣ (CLUBS)", "true"],
        ["TC02", "5 lá liên tiếp thứ bậc, khác chất (Sai chất lá cuối)", "2♣, 3♣, 4♣, 5♣, 6♦ (6 DIAMONDS)", "false"],
        ["TC03", "5 lá cùng chất, sai thứ tự liên tiếp (Nhảy bậc Rank)", "2♣, 3♣, 4♣, 5♣, 7♣ (SEVEN thay vì SIX)", "false"],
        ["TC04", "Duyệt toàn bộ dãy liên tiếp theo thứ tự ordinal", "4 chất × 9 dãy bắt đầu từ ACE đến NINE", "36 lần gọi đều true"],
        ["TC05", "Sảnh Royal Flush (10-J-Q-K-A)", "10♥, J♥, Q♥, K♥, A♥ (HEARTS)", "false (UUT gốc)"]
    ]
    add_table_data(doc, headers_tc, data_tc)

    add_section_heading(doc, "2.4. Phân tích số lần thực thi UUT: 40 sảnh Poker vs 36 sảnh theo ordinal()")
    add_paragraph(doc, "Phân tích sự khác biệt giữa luật Poker và hiện thực thuật toán:", bold_prefix="Phân tích chuyên sâu: ")
    add_bullet(doc, "Về mặt quy tắc Poker tiêu chuẩn: Mỗi chất có đúng 10 dãy 5 lá liên tiếp có thể tạo thành Straight Flush (từ A-2-3-4-5, 2-3-4-5-6, ..., đến 10-J-Q-K-A). Với 4 chất (Cơ, Rô, Nhép, Bích), tổng số Straight Flush hợp lệ theo luật là 4 × 10 = 40 bộ.")
    add_bullet(doc, "Về mặt thuật toán của UUT đề bài: Mã nguồn PokerHand sử dụng trực tiếp enum Rank và biểu thức card.rank().ordinal() - rank.ordinal() != 1 để kiểm tra tính liên tiếp. Trong enum Rank (ACE, TWO, THREE, ..., KING), ACE có ordinal là 0 và KING có ordinal là 12. Khi phương thức khởi tạo PokerHand sắp xếp các lá bài theo Rank, bộ bài 10-J-Q-K-A sẽ được xếp thành: ACE(0), TEN(9), JACK(10), QUEEN(11), KING(12). Khi duyệt vòng lặp, khoảng cách giữa lá thứ hai TEN(9) và lá đầu tiên ACE(0) là 9 - 0 = 9 != 1, do đó phương thức lập tức trả về false!")
    add_bullet(doc, "Kết luận: Thuật toán UUT của đề bài chỉ có thể nhận diện được các sảnh bắt đầu từ index 0 (ACE) đến index 8 (NINE), tức đúng 9 sảnh cho mỗi chất. Vì vậy, số lần thực thi UUT để kiểm tra tất cả các sảnh mà thuật toán nhận diện được là: 9 × 4 = 36 lần thực thi (trả về true). Bốn sảnh Royal Flush (10-J-Q-K-A) còn lại bị thuật toán trả về false.")

    add_section_heading(doc, "2.5. Làm rõ việc bảo toàn thuật toán UUT và các hiệu chỉnh phụ trợ của lớp PokerHand")
    add_paragraph(doc, "Để trung thực tuyệt đối với đề bài và bảo đảm tính minh bạch trong báo cáo:", bold_prefix="Minh bạch mã nguồn: ")
    add_bullet(doc, "Về phương thức kiểm thử UUT isStraightFlush(): Thuật toán kiểm tra và logic xử lý được giữ nguyên vẹn 100% đúng như mã nguồn giáo trình cung cấp (bao gồm cả giới hạn bỏ sót 4 sảnh Royal Flush do giả định ordinal của quân ACE).")
    add_bullet(doc, "Về lớp PokerHand: Có 2 hiệu chỉnh kỹ thuật phụ trợ nhỏ: (1) Thêm kiểm tra pCards != null trong câu lệnh assert của constructor để tăng tính lập trình phòng thủ (assert pCards != null && pCards.length == 5;); (2) Bổ sung 2 phương thức getter cards() và getCards() trả về danh sách bất biến Collections.unmodifiableList(aCards) để tiện quan sát trạng thái mà vẫn bảo đảm tính đóng gói.")
    add_bullet(doc, "Kiểm chứng đặc tả (Characterization Test): Để ghi nhận lỗi Royal Flush mà không sửa UUT, ta xây dựng ca kiểm thử testRoyalFlushKnownLimitation() khẳng định assertFalse(invokeIsStraightFlush(hand)). Test này PASS vì kiểm chứng đúng hành vi thực tế của UUT gốc.")

    add_section_heading(doc, "2.6. Phân tích kiểm thử vét cạn (Exhaustive Testing) và Không gian mẫu")
    add_paragraph(doc, "Trả lời câu hỏi: Kiểm thử tất cả các bộ Straight Flush có đồng nghĩa với việc đạt kiểm thử vét cạn (exhaustive test coverage) không?", bold_prefix="Khẳng định lý thuyết: ")
    add_bullet(doc, "Khẳng định: Tuyệt đối KHÔNG.")
    add_bullet(doc, "Phân tích không gian mẫu: Tổng số tổ hợp 5 lá bài khả dĩ được rút ngẫu nhiên từ bộ bài 52 lá là C(52, 5) = 2,598,960 bộ bài.")
    add_bullet(doc, "Phân chia không gian kết quả mong đợi theo luật Poker: Đúng 40 bộ Straight Flush có kết quả mong đợi là true. Còn lại 2,598,920 bộ bài khác (bài rác, bài đôi, hai đôi, sám cô, cù lũ, sảnh khác chất...) đều có kết quả nghiệp vụ mong đợi là false.")
    add_bullet(doc, "Đánh giá tỷ lệ bao phủ: 36 ca kiểm thử nhận diện của UUT chỉ chiếm 36 / 2,598,960 ≈ 0.001385% không gian đầu vào (nếu tính đủ 40 ca theo luật Poker cũng chỉ chiếm 0.001539%). Hơn 2.59 triệu trường hợp có kết quả mong đợi là false hoàn toàn chưa được kiểm thử nếu chỉ chạy các sảnh Straight Flush. Do đó, kiểm thử vét cạn là bất khả thi nếu chỉ dựa vào tập đầu vào dương tính.")

    # =========================================================================
    # CHƯƠNG 3
    # =========================================================================
    add_chapter_heading(doc, "CHƯƠNG 3. EXERCISE 5.9 – STATEMENT COVERAGE (ĐỘ BAO PHỦ CÂU LỆNH)")
    
    add_section_heading(doc, "3.1. Cơ sở lý thuyết Statement Coverage")
    add_paragraph(doc, "Statement Coverage (Độ bao phủ câu lệnh) là tiêu chí kiểm thử hộp trắng đo lường tỷ lệ các câu lệnh thực thi trong mã nguồn đã được thực hiện ít nhất một lần trong quá trình chạy kiểm thử.")
    add_paragraph(doc, "Statement Coverage = (Số câu lệnh thực thi ít nhất một lần / Tổng số câu lệnh thực thi) × 100%", bold_prefix="Công thức: ")

    add_section_heading(doc, "3.2. Phân rã câu lệnh mã nguồn của phương thức isStraightFlush()")
    add_paragraph(doc, "Bảng phân tích 10 câu lệnh có thể thực thi trong phương thức:")
    
    headers_stmt_decomp = ["Mã câu lệnh", "Dòng mã nguồn (Source Code)", "Ý nghĩa thực thi"]
    data_stmt_decomp = [
        ["S1", "Suit suit = aCards.get(0).suit();", "Lấy chất của lá bài đầu tiên làm mốc so sánh"],
        ["S2", "Rank rank = aCards.get(0).rank();", "Lấy thứ bậc của lá bài đầu tiên làm mốc so sánh"],
        ["S3", "Iterator<Card> iterator = aCards.iterator();", "Khởi tạo Iterator duyệt danh sách bài"],
        ["S4", "iterator.next();", "Bỏ qua lá bài đầu tiên"],
        ["S5", "while (iterator.hasNext())", "Vòng lặp kiểm tra 4 lá bài kế tiếp"],
        ["S6", "Card card = iterator.next();", "Lấy lá bài kế tiếp trong danh sách"],
        ["S7", "if (card.suit() != suit || rankGap != 1)", "Kiểm tra điều kiện sai chất hoặc sai bậc"],
        ["S8", "return false;", "Thoát sớm khi phát hiện lá bài không hợp lệ"],
        ["S9", "rank = card.rank();", "Cập nhật mốc bậc cho vòng lặp tiếp theo"],
        ["S10", "return true;", "Trả về kết quả thành công khi duyệt hết 4 lá"]
    ]
    add_table_data(doc, headers_stmt_decomp, data_stmt_decomp)

    add_section_heading(doc, "3.3. Thiết kế bộ kiểm thử tối thiểu và Ma trận bao phủ câu lệnh")
    add_paragraph(doc, "Để đạt 100% Statement Coverage với số lần thực thi UUT tối thiểu, chúng ta cần phân tích luồng thực thi:")
    add_bullet(doc, "Lần thực thi 1 (TC01 - Bộ bài hợp lệ): Đi qua S1, S2, S3, S4, S5, S6, S7, S9 (lặp 4 lần) và kết thúc tại S10 (return true). Tổng cộng 9/10 câu lệnh được thực thi. Riêng câu lệnh S8 (return false) chưa bao giờ được chạy.")
    add_bullet(doc, "Lần thực thi 2 (TC02 - Bộ bài có lá sai chất): Ngay tại lá bài vi phạm đầu tiên, điều kiện if thỏa mãn, luồng thực thi nhảy vào thực hiện câu lệnh S8 (return false) và kết thúc hàm ngay lập tức.")
    
    headers_stmt_mat = ["Câu lệnh", "S1", "S2", "S3", "S4", "S5", "S6", "S7", "S8", "S9", "S10", "Tỷ lệ bao phủ"]
    data_stmt_mat = [
        ["TC01 (True)", "✓", "✓", "✓", "✓", "✓", "✓", "✓", "-", "✓", "✓", "9/10 (90%)"],
        ["TC02 (False)", "✓", "✓", "✓", "✓", "✓", "✓", "✓", "✓", "-", "-", "8/10 (80%)"],
        ["Tổng hợp TC01 + TC02", "✓", "✓", "✓", "✓", "✓", "✓", "✓", "✓", "✓", "✓", "10/10 (100%)"]
    ]
    add_table_data(doc, headers_stmt_mat, data_stmt_mat)

    add_callout_box(doc, "KẾT LUẬN SỐ LẦN THỰC THI TỐI THIỂU CHO STATEMENT COVERAGE",
                    "Đối với phương thức isStraightFlush(), cần TỐI THIỂU ĐÚNG 2 LẦN GỌI UUT (1 ca true và 1 ca false) để đạt 100% Statement Coverage hoàn chỉnh. Một lần gọi duy nhất không bao giờ có thể đạt 100% vì phương thức có 2 câu lệnh return loại trừ lẫn nhau.", "info")
    
    add_paragraph(doc, "Lưu ý phương pháp luận: Kết luận 100% Statement Coverage nêu trên được suy luận trực tiếp từ phân tích cấu trúc luồng điều khiển mã nguồn (Static Control-Flow Analysis), không phải số đo do công cụ bytecode profiling JaCoCo thực hiện trên máy ảo.", bold_prefix="Ghi chú học thuật: ")

    # =========================================================================
    # CHƯƠNG 4
    # =========================================================================
    add_chapter_heading(doc, "CHƯƠNG 4. EXERCISE 5.10 – BRANCH COVERAGE (ĐỘ BAO PHỦ NHÁNH)")
    
    add_section_heading(doc, "4.1. Cơ sở lý thuyết Branch Coverage và Quan điểm của tác giả")
    add_paragraph(doc, "Branch Coverage (Độ bao phủ nhánh) yêu cầu mỗi nhánh rẽ khả dĩ xuất phát từ các điểm quyết định (vòng lặp, rẽ nhánh điều kiện) đều phải được thực thi ít nhất một lần.")
    add_paragraph(doc, "Trong giáo trình của tác giả Martin P. Robillard, đối với các biểu thức điều kiện phức hợp sử dụng toán tử ngắn mạch (short-circuit boolean operators như || hoặc &&), mỗi vế điều kiện con được biên dịch thành một điểm rẽ nhánh logic độc lập trong bytecode. Do đó, Branch Coverage đầy đủ đòi hỏi phải kiểm thử độc lập các nhánh rẽ phát sinh từ từng toán hạng.")

    add_section_heading(doc, "4.2. Phân tích các điểm rẽ nhánh và cơ chế ngắn mạch của câu lệnh IF")
    add_paragraph(doc, "Trong phương thức isStraightFlush(), câu lệnh rẽ nhánh trọng tâm là:")
    add_code_box(doc, "if (card.suit() != suit || card.rank().ordinal() - rank.ordinal() != 1)")
    
    add_paragraph(doc, "Cơ chế đánh giá ngắn mạch của toán tử OR (||) tạo ra 3 nhánh điều khiển độc lập:", bold_prefix="Ba nhánh rẽ điều khiển: ")
    add_bullet(doc, "Nhánh 1 (Vế 1 True): card.suit() != suit là True (khác chất). Cơ chế ngắn mạch ngắt ngay lập tức, không đánh giá vế 2, nhảy vào thân if thực hiện S8 (return false).")
    add_bullet(doc, "Nhánh 2 (Vế 1 False, Vế 2 True): card.suit() != suit là False (cùng chất), bắt buộc phải đánh giá vế 2. Vế 2 card.rank().ordinal() - rank.ordinal() != 1 là True (sai khoảng cách rank). Nhảy vào thân if thực hiện S8 (return false).")
    add_bullet(doc, "Nhánh 3 (Vế 1 False, Vế 2 False): Cả hai vế đều False. Bỏ qua if, cập nhật rank = card.rank() và tiếp tục vòng lặp (khi duyệt xong 4 vòng lặp thì đi đến S10 return true).")

    add_section_heading(doc, "4.3. Kết luận dứt khoát về số lần thực thi tối thiểu cho Branch Coverage")
    add_paragraph(doc, "Dựa trên phân tích cấu trúc điều khiển theo chuẩn giáo trình:", bold_prefix="KẾT LUẬN DỨT KHOÁT: ")
    add_paragraph(doc, "Số lần thực thi UUT tối thiểu để đạt 100% Branch Coverage cho phương thức isStraightFlush() là ĐÚNG 3 LẦN THỰC THI (sử dụng bộ 3 ca kiểm thử: TC01 - Hợp lệ, TC02 - Sai chất, TC03 - Sai bậc).")
    
    add_callout_box(doc, "ĐÁP ÁN CHÍNH THỨC CHO BÀI TẬP 5.10",
                    "Đáp án chính thức: CẦN TỐI THIỂU 3 LẦN THỰC THI UUT để đạt 100% Branch Coverage (TC01 đi qua nhánh bỏ qua if để return true; TC02 đi qua nhánh vi phạm chất ngắt ngắn mạch; TC03 đi qua nhánh vi phạm thứ bậc rank).", "info")

    add_paragraph(doc, "Ghi chú học thuật mở rộng: Trong một số giáo trình lý thuyết kiểm thử cơ bản chỉ định nghĩa Decision Coverage ở mức câu lệnh if tổng thể (coi toàn bộ biểu thức if chỉ có 2 nhánh kết quả: True để vào thân if hoặc False để bỏ qua if mà không phân tích cấu trúc ngắn mạch bên trong), thì 2 lần gọi (TC01 và TC02) là đủ để đi qua 2 nhánh của lệnh if và 2 nhánh của vòng lặp while. Tuy nhiên, tác giả bài tập phân tích sâu ở cấp độ toán tử ngắn mạch, do đó đáp án dứt khoát và chuẩn mực là 3 lần thực thi.", bold_prefix="Ghi chú mở rộng lý thuyết: ")

    add_section_heading(doc, "4.4. Bảng ma trận đánh giá các nhánh rẽ và điều kiện ngắn mạch")
    add_paragraph(doc, "Bảng ma trận kích hoạt toàn diện 3 ca kiểm thử cho Branch Coverage:")
    
    headers_branch_mat = ["Ca Test", "Vế 1 (Suit khác?)", "Vế 2 (Rank sai?)", "Nhánh rẽ thực thi", "Đường đi câu lệnh", "Kết quả"]
    data_branch_mat = [
        ["TC01", "False (4 lần)", "False (4 lần)", "Bỏ qua IF", "S1->S7 -> S9 -> ... -> S10", "true"],
        ["TC02", "True (Ngắn mạch ngắt ngay)", "Không đánh giá", "Nhảy vào IF", "S1->S7 -> S8 (return false)", "false"],
        ["TC03", "False (Cùng chất)", "True (Sai bậc)", "Nhảy vào IF", "S1->S7 -> S8 (return false)", "false"]
    ]
    add_table_data(doc, headers_branch_mat, data_branch_mat)

    # =========================================================================
    # CHƯƠNG 5
    # =========================================================================
    add_chapter_heading(doc, "CHƯƠNG 5. KẾT QUẢ THỰC THI VÀ TỔNG HỢP SO SÁNH TOÀN DIỆN")
    
    add_section_heading(doc, "5.1. Môi trường thực nghiệm và Kiến trúc TestRunner")
    add_paragraph(doc, "Môi trường thực thi thực tế của dự án:", bold_prefix="Môi trường hệ thống: ")
    add_bullet(doc, "Trình biên dịch & Máy ảo: Oracle JDK 25.0.1 (LTS 2025).")
    add_bullet(doc, "Cấu hình biên dịch tương thích: Sử dụng tùy chọn javac --release 21 để sinh ra mã bytecode class major version 65 (Java 21 LTS), khớp hoàn toàn với cấu hình <maven.compiler.release>21</maven.compiler.release> trong pom.xml.")
    add_bullet(doc, "Thư viện kiểm thử: Thư mục lib/ tích hợp sẵn 6 file JAR JUnit Jupiter 5.10.2 (API, Engine, Platform Commons, Platform Engine, OpenTest4j, APIGuardian). Dự án hoàn toàn độc lập, không cần kết nối Internet và không cần cài đặt Maven.")
    add_bullet(doc, "Kiến trúc TestRunner: TestRunner là một bộ chạy kiểm thử console độc lập được tự xây dựng bằng Java Reflection API. Bộ chạy tự động quét runtime tất cả các phương thức có gắn annotation @Test (org.junit.jupiter.api.Test) trong TestPokerHand, kích hoạt thực thi (method.invoke), bắt ngoại lệ AssertionError, thống kê số test PASSED/FAILED và in báo cáo định dạng console. TestRunner không sử dụng org.junit.platform.launcher và không cần thư viện launcher.")

    add_section_heading(doc, "5.2. Nhật ký thực thi kiểm thử thực tế (Actual Execution Log)")
    add_paragraph(doc, "Dưới đây là nhật ký thực thi thực tế được trích xuất trực tiếp từ console sau khi biên dịch bằng javac --release 21:")
    
    actual_log = """================================================================================
             RUNNING AUTOMATED UNIT TESTS FOR POST_CLASS_06                     
       (Exercises 5.8, 5.9, 5.10 - Reflection, Statements & Branches)           
================================================================================

[TEST SUITE] TestPokerHand (Exercise 8, 9, 10 Suite)
  [PASS] testIsStraightFlush_True
  [PASS] testStraightFlushCountsAndExhaustiveAnalysis
  [PASS] testRoyalFlushKnownLimitation
  [PASS] testIsStraightFlush_WrongSuit
  [PASS] testAll36OrdinalConsecutiveHands
  [PASS] testIsStraightFlush_WrongRank
--------------------------------------------------------------------------------
OVERALL RESULTS: 6 PASSED, 0 FAILED, TOTAL TESTS: 6
================================================================================"""
    add_code_box(doc, actual_log)

    add_section_heading(doc, "5.3. Phân biệt quan trọng giữa 6 hàm @Test và 40 lần gọi UUT thực tế")
    add_paragraph(doc, "Để tránh nhầm lẫn giữa số phương thức kiểm thử tự động và số lần thực thi hàm mục tiêu:", bold_prefix="Lưu ý đếm số lượng: ")
    add_bullet(doc, "Số lượng phương thức @Test: Đúng bằng 6 phương thức (hiển thị trong báo cáo JUnit 5 ở trên).")
    add_bullet(doc, "Số lần thực tế kích hoạt phương thức UUT (isStraightFlush()): Tổng cộng đúng bằng 40 lần gọi, gồm: TC01 (1 lần) + TC02 (1 lần) + TC03 (1 lần) + testAll36OrdinalConsecutiveHands (36 lần) + testRoyalFlushKnownLimitation (1 lần).")
    add_bullet(doc, "Phương thức testStraightFlushCountsAndExhaustiveAnalysis không gọi UUT mà kiểm chứng logic toán học về số lượng sảnh và tỷ lệ không gian mẫu.")

    add_section_heading(doc, "5.4. Bảng tổng hợp so sánh giữa Exercise 5.8, 5.9 và 5.10")
    add_paragraph(doc, "Bảng đối chiếu tổng quan tiêu chí kiểm thử giữa ba bài tập:")
    
    headers_summary = ["Bài tập", "Ca kiểm thử sử dụng", "Mục tiêu & Tiêu chí bao phủ", "Số lần gọi UUT tối thiểu", "Kết quả thực tế"]
    data_summary = [
        ["Exercise 5.8", "TC01 (True)\n+ 36 sảnh ordinal\n+ Royal Flush", "Kiểm thử private qua Reflection;\nKiểm chứng sảnh hợp lệ;\nPhân tích không gian mẫu vét cạn", "1 lần (cho ca true)\nhoặc 36 lần (mọi sảnh UUT)", "Đạt 100% chức năng;\nChứng minh không thể vét cạn (36/2.59M)"],
        ["Exercise 5.9", "TC01 (True)\n+ TC02 (False - Sai chất)", "Statement Coverage: Đảm bảo 10/10 câu lệnh (S1..S10) được thực thi ít nhất 1 lần", "2 lần gọi", "Đạt 100% Statement Coverage"],
        ["Exercise 5.10", "TC01 (True)\n+ TC02 (Sai chất)\n+ TC03 (Sai bậc)", "Branch Coverage: Bao phủ cả 3 nhánh rẽ logic của IF ngắn mạch và WHILE", "3 lần gọi", "Đạt 100% Branch Coverage"]
    ]
    add_table_data(doc, headers_summary, data_summary)

    # =========================================================================
    # CHƯƠNG 6
    # =========================================================================
    add_chapter_heading(doc, "CHƯƠNG 6. KẾT LUẬN VÀ TÀI LIỆU THAM KHẢO")
    
    add_section_heading(doc, "6.1. Đánh giá kết quả đạt được")
    add_paragraph(doc, "Thông qua bài tập Post Class 06, toàn bộ các yêu cầu của 3 bài tập 5.8, 5.9 và 5.10 đã được giải quyết trọn vẹn, chính xác và thống nhất:")
    add_bullet(doc, "Áp dụng thành công Java Reflection API để kiểm thử phương thức private một cách chuyên nghiệp.")
    add_bullet(doc, "Làm sáng tỏ sự khác biệt giữa 40 sảnh theo luật Poker và 36 sảnh được thuật toán UUT nhận diện, ghi nhận hạn chế Royal Flush bằng kiểm thử đặc tả mà vẫn giữ nguyên vẹn thuật toán UUT.")
    add_bullet(doc, "Chứng minh toán học tính bất khả thi của kiểm thử vét cạn dựa trên phân tích không gian tổ hợp 2,598,960 tay bài.")
    add_bullet(doc, "Đạt 100% Statement Coverage với tối thiểu đúng 2 lần gọi UUT và 100% Branch Coverage với tối thiểu đúng 3 lần gọi UUT.")

    add_section_heading(doc, "6.2. Bài học kinh nghiệm trong kiểm thử phần mềm")
    add_bullet(doc, "Độ bao phủ cao không đồng nghĩa với phần mềm không có lỗi: Phương thức isStraightFlush() đạt 100% cả Statement Coverage và Branch Coverage nhưng vẫn bỏ sót toàn bộ 4 sảnh Royal Flush do giả định sai về ordinal của quân ACE.")
    add_bullet(doc, "Cần kết hợp hài hòa giữa kiểm thử hộp trắng (White-box Testing) để đạt độ bao phủ cấu trúc và kiểm thử hộp đen (Black-box Testing) dựa trên đặc tả nghiệp vụ để phát hiện lỗi logic tiềm ẩn.")
    add_bullet(doc, "Khi kiểm thử các biểu thức điều kiện phức hợp chứa toán tử ngắn mạch (&&, ||), cần thiết kế ca test kích hoạt độc lập từng vế điều kiện để tránh hiện tượng che giấu lỗi (fault masking).")

    add_section_heading(doc, "6.3. Tài liệu tham khảo (References)")
    add_paragraph(doc, "[1] Martin P. Robillard, Introduction to Software Design with Java, 3rd Edition, Springer, 2025.")
    add_paragraph(doc, "[2] Martin P. Robillard, Mã nguồn mở bài tập và giải pháp DesignBook: https://github.com/prmr/DesignBook (exercises chapter 5 và e3.chapter5.PokerHand).")
    add_paragraph(doc, "[3] JUnit Team, JUnit 5 User Guide (JUnit Jupiter 5.10.2): https://junit.org/junit5/docs/current/user-guide/.")
    add_paragraph(doc, "[4] Oracle Corporation, Java Platform Standard Edition Reflection API Documentation (java.lang.reflect.Method).")

    # Save to both target locations
    out_master = r"d:\MY STUDY\HK1_4\THIET_KE\Post_Class_06\BAO_CAO_POST_CLASS_06_HOAN_CHINH_MASTER.docx"
    out_main = r"d:\MY STUDY\HK1_4\THIET_KE\Post_Class_06\BAO_CAO_POST_CLASS_06.docx"
    
    doc.save(out_master)
    doc.save(out_main)
    print(f"Master report saved successfully to:\n  - {out_master}\n  - {out_main}")

if __name__ == '__main__':
    build_perfect_report()
