<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Add Question</title>
    <style>
      body {
        font-family: sans-serif;
        margin: 0;
        padding: 0;
        display: flex;
        flex-direction: column;
        min-height: 100vh;
      }
      main {
        padding: 2rem;
        flex: 1;
      }
      .form-group {
        margin-bottom: 1rem;
      }
      label {
        display: block;
        margin-bottom: 0.5rem;
        font-weight: bold;
      }
      input[type="text"],
      textarea,
      select,
      input[type="number"] {
        width: 100%;
        padding: 0.5rem;
        box-sizing: border-box;
      }
      .options-group {
        background: #f9f9f9;
        padding: 1rem;
        border-radius: 4px;
        margin-top: 0.5rem;
      }
    </style>
    <script>
      function toggleOptions() {
        var type = document.getElementById("qtype").value;
        var mcq = document.getElementById("mcq-options");
        if (type === "MCQ") {
          mcq.style.display = "block";
        } else {
          mcq.style.display = "none";
        }
      }
    </script>
  </head>
  <body>
    <jsp:include page="../common/header.jsp" />
    <div style="display: flex; flex: 1">
      <jsp:include page="../common/sidebar.jsp" />
      <main>
        <h2>Add Question to Exam #${exam_id}</h2>
        <form action="controller" method="post">
          <input type="hidden" name="action" value="add_questions" />
          <input type="hidden" name="exam_id" value="${exam_id}" />

          <div class="form-group">
            <label>Question Content</label>
            <textarea name="content" rows="3" required></textarea>
          </div>

          <div class="form-group">
            <label>Question Type</label>
            <select name="type" id="qtype" onchange="toggleOptions()">
              <option value="MCQ">Multiple Choice</option>
              <option value="TRUE_FALSE">True / False</option>
              <option value="SHORT_ANSWER">Short Answer</option>
            </select>
          </div>

          <div id="mcq-options" class="options-group">
            <div class="form-group">
              <label>Option A</label><input type="text" name="option_a" />
            </div>
            <div class="form-group">
              <label>Option B</label><input type="text" name="option_b" />
            </div>
            <div class="form-group">
              <label>Option C</label><input type="text" name="option_c" />
            </div>
            <div class="form-group">
              <label>Option D</label><input type="text" name="option_d" />
            </div>
          </div>

          <div class="form-group">
            <label>Correct Answer</label>
            <input
              type="text"
              name="correct_answer"
              placeholder="For MCQ: 'A', 'B' | For T/F: 'TRUE' | For Short: Answer Text"
              required
            />
            <small
              >Enter 'A', 'B', 'C', 'D' for MCQ. 'TRUE'/'FALSE' for Boolean.
              Full text for Short Answer.</small
            >
          </div>

          <div class="form-group">
            <label>Marks</label>
            <input type="number" name="marks" value="1" min="1" required />
          </div>

          <button
            type="submit"
            style="
              padding: 0.5rem 1rem;
              background-color: #28a745;
              color: white;
              border: none;
            "
          >
            Add Question
          </button>
          <a href="controller?action=view_exams" style="margin-left: 1rem"
            >Done / Back to Exams</a
          >
        </form>
      </main>
    </div>
    <jsp:include page="../common/footer.jsp" />
  </body>
</html>
