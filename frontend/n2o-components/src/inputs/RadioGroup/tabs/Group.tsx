import React from 'react'

import { Group as GroupDefault, Props } from '../default/Group'

import { InputRadio } from './Input'

export function Group(props: Props) {
    return (<GroupDefault {...props} InputComponent={InputRadio} groupClassName="n2o-radio-group-tabs" />
    )
}
