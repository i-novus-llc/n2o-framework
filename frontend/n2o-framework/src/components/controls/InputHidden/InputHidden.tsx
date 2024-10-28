import React from 'react'

import { Input, type Props } from '../Input/Input'

function InputHidden(props: Props) {
    return <Input {...props} type="hidden" />
}

export default InputHidden
