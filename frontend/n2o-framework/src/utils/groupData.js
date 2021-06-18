const groupData = (data, field) => data.reduce((r, a) => {
    r[a[field]] = r[a[field]] || []
    r[a[field]].push(a)

    return r
}, Object.create(null))

export default groupData
