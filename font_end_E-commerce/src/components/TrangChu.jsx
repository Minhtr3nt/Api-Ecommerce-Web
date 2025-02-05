import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

export default function TrangChu() {
  const [devices, setDevices] = useState([]);
  const [imageUrls, setImageUrls] = useState({});
  const navigate = useNavigate();


  useEffect(() => {
    const getProduct = async () => {
      try {
        const response = await fetch("http://localhost:8081/api/v1/products/all", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        });
        const res = await response.json();
        setDevices(res.data);
      } catch (err) {
        console.log(err);
      }
    };
    getProduct();
  }, []);

  useEffect(() => {
    const fetchImages = async () => {
      const newImageUrls = {};

      for (const device of devices) {
        if (device.images && device.images.length > 0) {
          try {
            const response = await fetch(
              `http://localhost:8081/api/v1/images/image/download/${device.images[0].imageId}`,
              {
                method: "GET",
              }
            );
            if (response.ok) {
              const blob = await response.blob();
              const imgUrl = URL.createObjectURL(blob);
              newImageUrls[device.id] = imgUrl;
            } else {
              newImageUrls[device.id] = "/placeholder.jpg";
            }
          } catch (error) {
            console.error("Error fetching image:", error);
            newImageUrls[device.id] = "/placeholder.jpg";
          }
        } else {
          newImageUrls[device.id] = "/placeholder.jpg";
        }
      }

      setImageUrls(newImageUrls);
    };

    if (devices.length > 0) {
      fetchImages();
    }
  }, [devices]);

  const handleXTC = (id) => {
    navigate("/XemChiTiet", {state: {id: id}}); // Chuyển hướng đến đường dẫn chi tiết sản phẩm
  };
  

  return (
    <div className="d-flex flex-column min-vh-100">
      
      

      {/* Main Content */}
      <main className="container flex-grow-1 py-4">
        {devices.length === 0 ? (
          <div className="text-center">No products found.</div>
        ) : (
          <div className="row row-cols-1 row-cols-md-4 g-4">
            {devices.map((device) => (
              <div className="col" key={device.id}>
                <div className="card h-100 shadow-sm">
                  {imageUrls[device.id] && (
                    <img
                      src={imageUrls[device.id]}
                      alt={device.name}
                      className="card-img-top"
                      style={{ 
                        height: "200px", 
                        objectFit: "contain",
                        padding: "10px"
                      }}
                    />
                  )}
                  <div className="card-body">
                    <h5 className="card-title">{device.name}</h5>
                    <p className="card-text text-muted">{device.description}</p>
                    <p className="fw-bold">COST: {device.price}$</p>
                    <p><button className="btn btn-primary btn-center" onClick={()=>handleXTC(device.id)}>Xem Chi Tiết</button></p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </main>

     {/* Footer */}
     <footer className="bg-dark text-white py-3 mt-auto">
        <div className="container text-center">
          <p className="mb-1">Contact us: support@example.com | Phone: +123 456 789</p>
          <p className="mb-0">&copy; 2025 My Products. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
}
