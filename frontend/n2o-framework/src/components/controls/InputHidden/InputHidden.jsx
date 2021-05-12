import React from 'react'

import Input from '../Input/Input'

function InputHidden({ id, ...props }) {
    return <Input {...props} type="hidden" />
}

InputHidden.propTypes = Input.propTypes

export default InputHidden
