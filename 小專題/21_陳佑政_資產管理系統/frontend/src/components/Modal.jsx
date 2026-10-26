// 由 React 控制開關的 Bootstrap 對話框 (取代原本的 prompt())
export default function Modal({ title, onClose, onSubmit, submitText = '儲存資料', children }) {
  const handleSubmit = (e) => {
    e.preventDefault()
    onSubmit()
  }

  return (
    <>
      <div className="modal d-block" tabIndex="-1" onClick={onClose}>
        <div className="modal-dialog" onClick={(e) => e.stopPropagation()}>
          <form className="modal-content" onSubmit={handleSubmit}>
            <div className="modal-header">
              <h5 className="modal-title">{title}</h5>
              <button type="button" className="btn-close" aria-label="Close" onClick={onClose}></button>
            </div>
            <div className="modal-body">{children}</div>
            <div className="modal-footer">
              <button type="button" className="btn btn-secondary" onClick={onClose}>取消</button>
              <button type="submit" className="btn btn-primary">{submitText}</button>
            </div>
          </form>
        </div>
      </div>
      <div className="modal-backdrop show"></div>
    </>
  )
}
