export function getCookie(name: string) {
    if (!document?.cookie) { return null }

    const cookie = document.cookie.split(';')
        .find(cookie => cookie.trim().startsWith(`${name}=`))

    return cookie ? cookie.split('=')[1].trim() : null
}
