import React, { ReactNode, useEffect, useRef, useState } from 'react'
import DrawerRC from 'rc-drawer'
import classNames from 'classnames'
import { IPlacement } from 'rc-drawer/lib/IDrawerPropTypes'

import { TBaseProps } from '../types'

export type Props = TBaseProps & {
    // Включение маски(backdrop)
    animation: boolean;
    // Позиция компонента
    backdrop: boolean;
    // Нижняя часть компонента
    children?: ReactNode;
    // Включение закрытия по клику на маску(backdrop)
    closable: boolean;
    closeOnBackdrop: boolean;
    // Включение кнопки закрытия
    closeOnEscape: boolean;
    // Основная часть компонента
    fixedFooter: boolean;
    // Заголовок компонента
    footer?: string | ReactNode;
    // Ширина компонента
    height?: string | number;
    // Флаг фиксирования футера
    level: string | string[];
    // Сдвиг элемента или группы элементов при открытии (level={'all'} для сдвига всех эл-в)
    onClose(): void;
    // Функция закрытия по клику
    onHandleClick(): void;
    // Разрешить/запретить закрытие Drawer по клавише Esc
    placement?: IPlacement;
    // Высота компонента
    title?: string | ReactNode;
    // Включение анимации
    width?: string | number; // Функция закрытия по клику на крестик
}

export function Drawer({
    className,
    closeOnBackdrop,
    visible,
    placement,
    onClose,
    onHandleClick,
    backdrop,
    level,
    animation,
    width,
    height,
    title,
    footer,
    fixedFooter,
    children,
    closeOnEscape,
    closable,
}: Props) {
    const [paddingBottom, setPaddingBottom] = useState(0)
    const footerRef = useRef<HTMLDivElement>(null)

    useEffect(() => {
        if (footerRef.current) {
            setPaddingBottom(footerRef.current.clientHeight)
        }
    }, [visible, setPaddingBottom])

    return (
        <DrawerRC
            className={classNames('n2o-drawer n2o-snippet', className, {
                'without-close-button': !closable,
                'drawer-animation': animation,
            })}
            open={visible}
            width={width}
            height={height}
            placement={placement}
            onClose={onClose}
            showMask={backdrop}
            level={level}
            maskClosable={closeOnBackdrop}
            onHandleClick={onHandleClick}
            keyboard={closeOnEscape}
        >
            <div
                className="n2o-drawer-content-wrapper"
                style={fixedFooter ? { paddingBottom } : {}}
            >
                <div className="n2o-drawer-children-wrapper">
                    {title && <div className="drawer-title">{title}</div>}
                    <div className="drawer-children">{children}</div>
                    <div
                        className={classNames('drawer-footer', {
                            'drawer-footer--fixed': fixedFooter,
                        })}
                        ref={footerRef}
                    >
                        {footer}
                    </div>
                </div>
            </div>
        </DrawerRC>
    )
}

Drawer.defaultProps = {
    animation: true,
    backdrop: false,
    closeOnBackdrop: true,
    level: '',
    fixedFooter: false,
    closable: true,
    closeOnEscape: true,
    onClose: () => {},
    onHandleClick: () => {},
} as Props
