export const isURL = (link: string) => {
    // return /^(\w){2,5}:\/\/./.test(link)
    return (link.startsWith('http://') || link.startsWith('https://'))
}
