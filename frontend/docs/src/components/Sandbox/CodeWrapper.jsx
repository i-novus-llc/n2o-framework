import React, { memo, useState } from 'react'
import PropTypes from 'prop-types'

import { Header } from './Header'
import { Code } from './Code'
import style from './sandbox.module.scss'

function CodeWrapperBody({ projectId, filesMap }) {
    const fileNames = Object.keys(filesMap).sort((a, b) => a - b)
    const startFileName = filesMap.hasOwnProperty('index.page.xml') ? 'index.page.xml' : fileNames[0]

    const [visible, setVisible] = useState(false)
    const [activeFileName, setActiveFileName] = useState(startFileName)

    if (fileNames.length === 0) {
        return null
    }

    return (
        <div className={style.codeWrapper}>
            <div
                className={style.codeWrapperToggler}
                onClick={() => { setVisible(!visible) }}
            >
                {visible ? 'Скрыть код' : 'Показать код'}
            </div>

            {
                visible
                    ? (
                        <>
                            <Header
                                projectId={projectId}
                                activeFileName={activeFileName}
                                setActiveFileName={setActiveFileName}
                                filesMap={filesMap}
                            />

                            <Code fileName={activeFileName} source={filesMap[activeFileName]} />
                        </>
                    )
                    : null
            }
        </div>
    )
}

const CodeWrapper = memo(CodeWrapperBody)

CodeWrapper.propTypes = {
    projectId: PropTypes.string.isRequired,
    filesMap: PropTypes.objectOf(PropTypes.string).isRequired,
}

export { CodeWrapper }
