import React from 'react'

import Input from '../Input/Input'

function InputHidden(props) {
    return <Input {...props} type="hidden" />
}

InputHidden.propTypes = Input.propTypes

export default InputHidden
