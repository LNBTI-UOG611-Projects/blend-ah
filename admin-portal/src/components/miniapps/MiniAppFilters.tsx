type Props = {
  search: string
  status: string
  onSearchChange: (value: string) => void
  onStatusChange: (value: string) => void
}

export default function MiniAppFilters({
  search,
  status,
  onSearchChange,
  onStatusChange,
}: Props) {
  return (
    <div className="filter-bar">
      <div className="search-box">
        <span>⌕</span>
        <input
          type="search"
          placeholder="Search mini apps..."
          value={search}
          onChange={(e) => onSearchChange(e.target.value)}
        />
      </div>

      <select
        value={status}
        onChange={(e) => onStatusChange(e.target.value)}
        aria-label="Filter by status"
      >
        <option value="ALL">All Statuses</option>
        <option value="PUBLISHED">Published</option>
        <option value="UNPUBLISHED">Unpublished</option>
      </select>
    </div>
  )
}