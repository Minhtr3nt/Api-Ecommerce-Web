import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Login from "./components/Login";
import TrangChu from "./components/TrangChu";
import './App.css'
import XemChiTiet from "./components/XemChiTiet";
import TaiChinh from "./components/TaiChinh";
import GioHang from "./components/GioHang";

function App() {
  

  return (
    
    <Router>
      <div>
      <header className="bg-primary text-white py-3 shadow">
        <div className="container d-flex justify-content-between align-items-center">
          <h1 className="h4">ĐIỆN TỬ GIA DỤNG THÔNG MINH </h1>
          <ul className="nav">
            <li className="nav-item">
              <a href="/TrangChu" className="nav-link text-white">
                Trang Chủ
              </a>
            </li>
            <li className="nav-item">
              <a href="/GioHang" className="nav-link text-white">
                Giỏ Hàng
              </a>
            </li>
            <li className="nav-item">
              <a href="#contact" className="nav-link text-white">
                Liên Hệ
              </a>
            </li>
            <li className="nav-item">
              <a href="/TaiChinh" className="nav-link text-white">
                Tài Chính
              </a>
            </li>
          </ul>
        </div>
      </header>
        <Routes>
          <Route path="/" element={<Login/>}/>
          <Route path="/TrangChu" element={<TrangChu/>}/>
          <Route path="/XemChiTiet" element={<XemChiTiet/>}/>
          <Route path="/GioHang" element={<GioHang/>}/>
          <Route path="/TaiChinh" element={<TaiChinh/>}/>
          
        </Routes>
      </div>
       
    </Router>
    
  )
}

export default App
