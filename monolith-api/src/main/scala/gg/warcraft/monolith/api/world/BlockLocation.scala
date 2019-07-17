package gg.warcraft.monolith.api.world

import gg.warcraft.monolith.api.math.{Vector3f, Vector3i}

case class BlockLocation(
  world: World,
  translation: Vector3i
) {
  val x: Int = translation.x
  val y: Int = translation.y
  val z: Int = translation.z

  def add(x: Int, y: Int, z: Int): BlockLocation =
    copy(translation = translation.add(x, y, z))

  def add(vec: Vector3i): BlockLocation = add(vec.x, vec.y, vec.z)

  def add(loc: BlockLocation): BlockLocation = add(loc.x, loc.y, loc.z)

  def sub(x: Int, y: Int, z: Int): BlockLocation =
    copy(translation = translation.sub(x, y, z))

  def sub(vec: Vector3i): BlockLocation = sub(vec.x, vec.y, vec.z)

  def sub(loc: BlockLocation): BlockLocation = sub(loc.x, loc.y, loc.z)

  def toLocation: Location = Location(world, translation.toVector3f, Vector3f())

  /* Java interop */

  def getWorld: World = world
  def getTranslation: Vector3i = translation
  def getX: Int = translation.x
  def getY: Int = translation.y
  def getZ: Int = translation.z

  def withWorld(world: World): BlockLocation = copy(world = world)
  def withTranslation(translation: Vector3i): BlockLocation = copy(translation = translation)
  def withX(x: Int): BlockLocation = copy(translation = translation.withX(x))
  def withY(y: Int): BlockLocation = copy(translation = translation.withY(y))
  def withZ(z: Int): BlockLocation = copy(translation = translation.withZ(z))
}