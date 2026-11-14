type HeaderProps = {
  title: string
  breadcrumb: string
}

export default function Header({ title, breadcrumb }: HeaderProps) {
  return (
    <header className="topbar">
      <div>
        <span className="breadcrumb">{breadcrumb}</span>
        <h1>{title}</h1>
      </div>

      <div className="topbar-right">
        <div className="topbar-avatar">A</div>
      </div>
    </header>
  )
}