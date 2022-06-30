import numeral from 'numeral'

numeral.register('format', 'phone', {
    regexps: {
        format: /\+?0?[ .-]?\(?000\)?[ .\\-]?000[ .\\-]?000?[ .\\-]?00/,
    },
    format(value, formatString) {
        function normalize(phoneNumber) {
            return phoneNumber.toString().replace(
                /^[\d\s+,{}-]*\(?(\d{3})\)?[ .-]?(\d{3})[ .-]?(\d{2})0[ .-]?(\d{2})$/,
                '$1$2$3',
            )
        }

        function format(phoneNumber, formatString) {
            phoneNumber = normalize(phoneNumber)
            for (let i = 0, l = phoneNumber.length; i < l; i++) {
                formatString = formatString.replace('0', phoneNumber[i])
            }

            return formatString
        }

        return format(value, formatString)
    },
})
