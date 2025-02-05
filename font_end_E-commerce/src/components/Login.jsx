import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

export default function Login() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const checkToken = async () => {
      const token = localStorage.getItem("token");
      if (!token) return; 

      try {
        const response = await fetch("http://localhost:8081/api/v1/auth/validate", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ token }),
        });

        if (!response.ok) {
          throw new Error("Network response was not ok");
        }

        const data = await response.json();
        if (data.data) {
          console.log("Token hợp lệ:", data.message);
          navigate("/TrangChu");
        } else {
          console.log("Token không hợp lệ:", data.message);
          localStorage.removeItem("token"); 
        }
      } catch (err) {
        console.log(err);
      }
    };

    checkToken();
  }, [navigate]); // Thêm `navigate` vào dependency array

  async function handleSubmit(event) {
    event.preventDefault(); // Ngăn form reload
    setIsLoading(true); // Hiển thị trạng thái loading
    setError(""); // Reset lỗi
  
    const formData = new FormData(event.target);
    const email = formData.get("email");
    const password = formData.get("password");
  
    try {
      const response = await fetch("http://localhost:8081/api/v1/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });
  
      if (!response.ok) {
        throw new Error("Login failed: " + response.statusText);
      }
  
      const data = await response.json();
      console.log("API Response:", data);
  
      if (data.data && data.data.token) {
        // Lưu token và userID vào localStorage
        localStorage.setItem("token", data.data.token);
        localStorage.setItem("userID", data.data.id);
  
        console.log("Token stored successfully:", data.data.token);
        navigate("/TrangChu"); // Điều hướng đến trang chính
      } else {
        throw new Error("Token is missing in the response");
      }
    } catch (err) {
      setError("Invalid username or password");
      console.error("Error during login:", err);
    } finally {
      setIsLoading(false); // Tắt trạng thái loading
    }
  }
  

  return (
    <div
      className="d-flex justify-content-center align-items-center vh-100 vw-100 bg-light"
      style={{
        position: "absolute",
        top: "0",
        left: "0",
        width: "100%",
        height: "100%",
      }}
    >
      <div
        className="bg-white shadow p-5 rounded-3 w-100"
        style={{ maxWidth: "500px" }}
      >
        <h2 className="text-center mb-4">Login</h2>

        {error && (
          <div className="alert alert-danger text-center" role="alert">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label htmlFor="email" className="form-label">
              Email
            </label>
            <input
              type="email"
              name="email"
              id="email"
              className="form-control"
              placeholder="Enter your email"
              required
            />
          </div>

          <div className="mb-3">
            <label htmlFor="password" className="form-label">
              Password
            </label>
            <input
              type="password"
              name="password"
              id="password"
              className="form-control"
              placeholder="Enter your password"
              required
            />
          </div>

          <button
            type="submit"
            className="btn btn-primary w-100"
            disabled={isLoading}
          >
            {isLoading ? "Logging in..." : "Login"}
          </button>

          <div className="mt-3 text-center">
            <span className="text-muted">Not a Member? </span>
            <a href="/signup" className="text-decoration-none">
              Signup
            </a>
          </div>
        </form>
      </div>
    </div>
  );
}
