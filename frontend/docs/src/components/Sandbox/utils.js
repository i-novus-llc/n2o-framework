export function getFileLang(fileName) {
    const fileNameParts = fileName.split('.')

    return fileNameParts[fileNameParts.length - 1]
}
