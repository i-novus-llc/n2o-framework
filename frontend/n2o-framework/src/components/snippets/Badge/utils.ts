import { Position } from './enums'

export const isBadgeLeftPosition = (badgePosition: Position) => badgePosition === Position.Left

export const isBadgeRightPosition = (badgePosition: Position) => badgePosition !== Position.Left
