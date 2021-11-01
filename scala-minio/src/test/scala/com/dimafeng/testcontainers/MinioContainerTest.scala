package com.dimafeng.testcontainers

import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

class MinioContainerTest extends AnyFunSuite
  with BeforeAndAfterEach
  with ForAllTestContainer {

  override val container: MinioContainer = MinioContainer()

}
