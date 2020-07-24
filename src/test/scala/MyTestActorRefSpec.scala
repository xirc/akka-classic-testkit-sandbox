import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.{DefaultTimeout, ImplicitSender, TestActorRef, TestKit}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.util.Success

class MyTestActorRefSpec
  extends TestKit(ActorSystem("test-system"))
  with AnyWordSpecLike
  with Matchers
  with DefaultTimeout
{
  class MyActor extends Actor {
    private var nrOfPing: Int = 0
    def getNrOfPing: Int = nrOfPing
    override def receive: Receive = {
      case "ping" =>
        nrOfPing += 1
        sender() ! "pong"
    }
  }

  "use TestActorRef" in {
    implicit def self = testActor
    val actorRef = TestActorRef(new MyActor)
    actorRef.underlyingActor.getNrOfPing mustBe 0
    actorRef ! "ping"
    expectMsg("pong")
    actorRef.underlyingActor.getNrOfPing mustBe 1
  }

  "ask to TestActorRef" in {
    val actorRef = TestActorRef(new MyActor)
    val future = actorRef ? "ping"
    val Success(result: String) = future.value.get
    result must be("pong")
  }

  "test expecting exceptions" in {
    val actor = TestActorRef(new Actor {
      override def receive: Receive = {
        case "hello" => throw new IllegalArgumentException("boom")
      }
    })
    intercept[IllegalArgumentException] {
      actor.receive("hello")
    }
  }
}
