 import { useEffect, useState } from 'react'
import { api } from '../api.js'
import SummaryBoard from '../components/SummaryBoard.jsx'
import AssetSection from '../components/AssetSection.jsx'
import InvestmentSection from '../components/InvestmentSection.jsx'

// 對應原本的 frontpage.html
export default function FrontPage() {
  const [summary, setSummary] = useState(null)

  const loadSummary = () => api.getSummary().then(setSummary).catch((e) => console.error('讀取總覽失敗', e))

  useEffect(() => {
    loadSummary()
  }, [])

  const usdRate = summary ? Number(summary.usdRate) : 31

  return (
    <div className="page" style={{ backgroundImage: 'url(/image/cat3.jpg)' }}>
      <SummaryBoard summary={summary} />

      <AssetSection category="CASH" title="流動資金小計" addText="+ 新增流動資金"
                    presets={['現金(台幣)', '現金(美金)']} usdRate={usdRate} onChange={loadSummary} />

      <InvestmentSection total={summary?.investment} usdRate={usdRate} onChange={loadSummary} />

      <AssetSection category="FIXED" title="固定資產小計" addText="+ 新增固定資產"
                    presets={['房子', '車子']} usdRate={usdRate} onChange={loadSummary} />

      <AssetSection category="LIABILITY" title="負債小計" addText="+ 新增負債項目"
                    presets={['房屋貸款', '信用卡費']} danger usdRate={usdRate} onChange={loadSummary} />
    </div>
  )
}
