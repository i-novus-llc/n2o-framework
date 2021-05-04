import React from 'react'

import style from './spinner.module.scss'

export function Spinner() {
    return (
        <svg className={style.spinner} viewBox="0 0 50 50">
            <circle className={style.path} cx="25" cy="25" r="20" fill="none" strokeWidth="5"/>
        </svg>
    )
}
