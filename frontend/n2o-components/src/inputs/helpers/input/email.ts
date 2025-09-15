export const isValidEmail = (email: string): boolean => {
    if (!email) { return false }

    const [_, domain] = email.split('@')

    if (!domain || !domain.includes('.')) { return false }

    const domainParts = domain.split('.')
    const tld = domainParts.at(-1)

    if (!tld || tld.length < 2) { return false }

    const tempInput = document.createElement('input')

    tempInput.type = 'email'
    tempInput.value = email

    return tempInput.checkValidity()
}

export const DEFAULT_PLACEHOLDER = 'example@mail.com'
export const DEFAULT_INVALID_TEXT = 'Некорректный email'
