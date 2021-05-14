import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import cn from 'classnames'
import { compose, withProps } from 'recompose'

import Drawer from '../snippets/Drawer/Drawer'
import Spinner from '../snippets/Spinner/Spinner'
import Toolbar from '../buttons/Toolbar'

import withOverlayMethods from './withOverlayMethods'
import Page from './Page'

/**
 * Компонент, отображающий Drawer
 * @reactProps {string} pageId - id пейджа
 * @reactProps {string} name - имя модалки
 * @reactProps {boolean} visible - отображается модалка или нет
 * @reactProps {string} modalHeaderTitle - заголовок в хэдере
 * @reactProps {object} actions - объект экшнов
 * @reactProps {array} toolbar - массив, описывающий внений вид кнопок-экшенов
 * @reactProps {object} props - аргументы для экшенов-функций
 * @reactProps {boolean}  disabled - блокировка модалки
 * @reactProps {function}  hidePrompt - скрытие окна подтверждения
 * @example
 *  <DrawerPage props={props}
 *             actions={actions}
 *             name={name}
 *             pageId={pageId}
 *  />
 */

function DrawerPage(props) {
    const {
        src,
        pageUrl,
        pageId,
        pathMapping,
        queryMapping,
        visible,
        loading,
        modalHeaderTitle,
        footer,
        disabled,
        toolbar,
        actions,
        entityKey,
        width,
        height,
        placement,
        backdrop,
        level,
        closeOnBackdrop,
        animation,
        prompt,
        closeOverlay,
        fixedFooter,
        closeOnEscape,
        closable,
        ...rest
    } = props

    const pageMapping = {
        pathMapping,
        queryMapping,
    }

    const showSpinner = !visible || loading || typeof loading === 'undefined'
    const classes = cn({ 'd-none': loading })
    const withToolbar = get(props, 'metadata.src') !== 'SearchablePage'

    return (
        <div className="drawer-page-overlay">
            <Spinner
                className="drawer-spinner"
                loading={showSpinner}
                type="cover"
                color="light"
                transparent
            >
                <Drawer
                    visible={!loading && visible !== false}
                    onHandleClick={closeOverlay}
                    onClose={closeOverlay}
                    title={modalHeaderTitle}
                    backdrop={backdrop}
                    width={width}
                    height={height}
                    placement={placement}
                    level={level}
                    closeOnBackdrop={closeOnBackdrop}
                    animation={animation}
                    fixedFooter={fixedFooter}
                    closeOnEscape={closeOnEscape}
                    closable={closable}
                    footer={
                        toolbar ? (
                            <div
                                className={cn('n2o-modal-actions', {
                                    'n2o-disabled': disabled,
                                })}
                            >
                                <Toolbar toolbar={toolbar.bottomLeft} entityKey={entityKey} />
                                <Toolbar toolbar={toolbar.bottomCenter} entityKey={entityKey} />
                                <Toolbar toolbar={toolbar.bottomRight} entityKey={entityKey} />
                            </div>
                        ) : (
                            footer
                        )
                    }
                >
                    <div className={classes}>
                        {pageUrl ? (
                            <Page
                                pageUrl={pageUrl}
                                pageId={pageId}
                                pageMapping={pageMapping}
                                entityKey={entityKey}
                                needMetadata
                                withToolbar={withToolbar}
                                initSearchValue=""
                                isDrawerPage
                            />
                        ) : src ? (
                            rest.renderFromSrc(src)
                        ) : null}
                    </div>
                </Drawer>
            </Spinner>
        </div>
    )
}

export const DrawerWindow = DrawerPage

DrawerPage.propTypes = {
    pageId: PropTypes.string,
    visible: PropTypes.bool,
    modalHeaderTitle: PropTypes.string,
    name: PropTypes.string,
    props: PropTypes.object,
    close: PropTypes.func.isRequired,
    disabled: PropTypes.bool,
    fixedFooter: PropTypes.bool,
    closeOnEscape: PropTypes.bool,
    closable: PropTypes.bool,
}

export default compose(
    withOverlayMethods,
    withProps(props => ({
        closeOverlay: () => props.closeOverlay(props.prompt),
    })),
)(DrawerPage)
