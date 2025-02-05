import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

export default function XemChiTiet() {
  const [product, setProduct] = useState(null);
  const [imgLinks, setImgLinks] = useState([]); // Lưu danh sách liên kết ảnh
  const [imgs, setImgs] = useState([]); // Lưu danh sách ảnh tải xuống
  const [quantity, setQuantity] = useState(1); // Lưu số lượng
  const location = useLocation();
  const { id } = location.state || {}; // Lấy id từ state

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await fetch(
          `http://localhost:8081/api/v1/products/product/${id}/product`,
          {
            method: "GET",
          }
        );
        const data = await response.json();
        if (data.data) {
          setProduct(data.data);
        }
      } catch (err) {
        console.error("Error fetching product: ", err);
      }
    };

    const fetchImgLinks = async () => {
      try {
        const response = await fetch(
          `http://localhost:8081/api/v1/images/get-img-by-product/${id}`,
          {
            method: "GET",
          }
        );
        const data = await response.json();
        if (data.data) {
          setImgLinks(data.data); // Giả sử API trả về danh sách liên kết ảnh
        }
      } catch (err) {
        console.error("Error fetching image links: ", err);
      }
    };

    const fetchImgs = async () => {
      if (!imgLinks || imgLinks.length === 0) return;

      try {
        const images = await Promise.all(
          imgLinks.map(async (link) => {
            const response = await fetch(
              `http://localhost:8081/api/v1/images/image/download/${link.imageId}`,
              {
                method: "GET",
              }
            );
            return URL.createObjectURL(await response.blob()); // Tạo URL cho ảnh
          })
        );
        setImgs(images);
      } catch (err) {
        console.error("Error fetching images: ", err);
      }
    };

    fetchProduct();
    fetchImgLinks();

    // Sau khi lấy danh sách liên kết ảnh, tải ảnh về
    if (imgLinks.length > 0) {
      fetchImgs();
    }
  }, [id, imgLinks]); // Chạy lại khi `id` hoặc `imgLinks` thay đổi

  // Hàm xử lý nút Mua Hàng
  const handlebtnBuy = async () => {
    if (quantity < 1 || quantity > product.inventory) {
      alert("Vui lòng chọn số lượng hợp lệ");
      return;
    }
    const token = localStorage.getItem("token");
    const addCart  = await fetch(`http://localhost:8081/api/v1/cartItems/item/add?productId=${product.id}&quantity=${quantity}`,{
      method: "POST",
      headers: {
        "Content-Type" : "application/json",
        Authorization : `Bearer ${token}`,
      }
    })
    if(!addCart.ok){
      alert("Không thể thêm vào giỏ hàng hãy đăng nhập lại!!")
      return;
    }
    alert("Thêm sản phẩm vào giỏ hàng thành công hãy kiểm tra lại giỏ hàng!!");
    };
    
  

  return (
    <div className="container mt-5">
      {product && (
        <>
          <h2 className="text-center mb-4 display-5 text-primary">
            Chi tiết sản phẩm: {product.name}
          </h2>
          <div className="row align-items-center">
            <div className="col-md-6">
              <div className="row">
                {imgs.length > 0 ? (
                  imgs.map((img, index) => (
                    <div className="col-6 mb-4" key={index}>
                      <img
                        src={img}
                        alt={`Sản phẩm ${index + 1}`}
                        className="img-fluid rounded shadow-sm"
                        style={{ width: "100%", height: "auto" }}
                      />
                    </div>
                  ))
                ) : (
                  <p>Không có hình ảnh</p>
                )}
              </div>
            </div>
            <div className="col-md-6">
              <div className="card shadow-sm p-4">
                <h5 className="text-secondary">
                  <strong>Giá:</strong> {product.price}$
                </h5>
                <h5 className="text-secondary">
                  <strong>Thương hiệu:</strong> {product.brand}
                </h5>
                <h5 className="text-secondary">
                  <strong>Số lượng có:</strong> {product.inventory}
                </h5>
                <h5 className="text-secondary">
                  <strong>Mô tả:</strong> {product.description}
                </h5>
                <h5 className="text-secondary">
                  <strong>Loại:</strong> {product.category.name}
                </h5>
                <div className="mt-4">
                  <label htmlFor="quantity" className="form-label">
                    <strong>Chọn số lượng:</strong>
                  </label>
                  <input
                    type="number"
                    id="quantity"
                    className="form-control mb-3"
                    min="1"
                    value={quantity}
                    onChange={(e) => setQuantity(e.target.value)}
                  />
                  <button
                    className="btn btn-primary"
                    onClick={()=>handlebtnBuy()}
                  >
                    Mua Hàng
                  </button>
                </div>
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
}
