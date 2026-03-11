async function loadSession(){
    try{
        const response=await fetch("https://localhost:8080/sessions");
        if(!response.ok) throw new Error("Network response was not ok");
        const sessions =await response.json();
        displaySessions(sessions);
        return sessions;
    } catch(error){
        console.error("Failed to load sessions: ",error);
        return[];
    }
}
function computePlateaus(sessions){
    const result={};
    if(sessions.length<2) return result;
    const last =sessions[sessions.length-1];
    const prev=sessions[sessions.length-2];

    prev.exercises.forEach(ePrev=>{
        const name=ePrev.name.toLowerCase();
        const eLast=last.exercises.find(e=>e.name.toLowerCase()===name);
        if(eLast) result[name]=eLast.sets*eLast.reps*eLast.weight<=ePrev.sets*ePrev.reps*ePrev.weight;
    });
    return result;

}

function computeProgress(sessions){
    const result={};
    if(sessions.length<2) return result;
    const last=sessions[sessions.length-1];
    const prev=sessions[sessions.length-2];

    prev.exercises.forEach(ePrev=>{
        const name=ePrev.name.toLowerCase();
        const eLast=last.exercises.find(e=> e.name.toLowerCase()===name);
        if(eLast){
            const diff=(eLast.sets*eLast.reps*eLast.weight)-(ePrev.sets*ePrev.reps*ePrev.weight);
            result[name]=diff;
        }
    });
    return result;
}
function computeNextWeights(sessions){
    const result={};
    if(sessions.length<2) return result;
    const last=sessions[sessions.length-1];
    const prev=sessions[sessions.length-2];

    prev.exercises.forEach(ePrev=>{
        const name=ePrev.name.toLowerCase();
        const eLast=last.exercises.find(e=> e.name.toLowerCase()===name);
        if(eLast){
            let weight=eLast.weight;
            if(eLast.sets*eLast.reps*eLast.weight>ePrev.sets*ePrev.reps*ePrev.weight){
                weight+=2.5;
} else if(eLast.sets*eLast.reps*eLast.weight<ePrev.sets*ePrev.reps*ePrev.weight){
    weight-=2.5;
}  result[name]=weight;

        }

        });
    return result;
}


function displaySessions(sessions){
    const container=document.getElementById("workouts");
    container.innerHTML="";
    sessions.forEach(session=>{
        const sessionDiv=document.createElement("div");
        sessionDiv.className="session";

        const date=document.createElement("h3");
        date.textContent=`Workout Date: ${session.date}`;
        sessionDiv.appendChild(date);

        session.exercises.forEach(exercise=>{
            const ex=document.createElement("p");
            ex.textContent=`${exercise.name}:${exercise.sets} sets x ${exercise.reps} reps @${exercise.weight} lbs`;
            sessionDiv.appendChild(ex);
        });
        const volume=document.createElement("p");
        volume.textContent=`Total Volume: ${session.totalVolume}`;
        sessionDiv.appendChild(volume);

        container.appendChild(sessionDiv);
    });
}

async function submitWorkout(workout){
    try{
       const response = await fetch("http://localhost:8080/saveWorkout", {
              method: "POST",
              headers: {"Content-Type":"application/json"},
             body: JSON.stringify(workout)
});

        if(!response.ok) throw new Error("Failed to submit workout.");
        alert("Workout saved!");
        const sessions=await loadSession();

        const plateauResults = computePlateaus(sessions);
        const progressResults=computeProgress(sessions);
        const nextWeights=computeNextWeights(sessions);

        displayAnalysis(plateauResults, progressResults, nextWeights);

    }catch(error){
        console.error(error);
    }
}

function gatherFormData(){
    const date=document.getElementById("date").value;
    const exercises=[];
    document.querySelectorAll(".exercise").forEach(exDiv=>{
        const name=exDiv.querySelector(".name").value;
        const sets=parseInt(exDiv.querySelector(".sets").value);
        const reps=parseInt(exDiv.querySelector(".reps").value);
        const weight=parseFloat(exDiv.querySelector(".weight").value);
        exercises.push({name,sets,reps,weight});
    });
    return {date, exercises};
}

document.addEventListener("DOMContentLoaded",()=>{
    loadSession();
    const form=document.getElementById("sessionForm");

    form.addEventListener("submit",(e)=>{
        e.preventDefault();
        const workout=gatherFormData();
        submitWorkout(workout);
    });
    document.getElementById("addExercise").addEventListener("click",()=>{
        const container =document.getElementById("exercises-container");
        const exDiv=document.createElement("div");
        exDiv.className="exercise";
        exDiv.innerHTML=`
            <input type="text" class="name" placeholder="Exercise Name" required>
            <input type="number" class="sets" placeholder="Sets" required>
            <input type="number" class="reps" placeholder="Reps" required>
            <input type="number" class="weight" placeholder="Weight (lbs)" min="0" required>
            `;
        container.appendChild(exDiv);
});
});
         const dateinput=document.getElementById("date");
            const today =new Date().toISOString().split("T")[0];
            dateinput.setAttribute("max",today);
            dateinput.value=today;

function displayAnalysis(plateaus, progress, nextWeights){
    const plateauDiv=document.getElementById("plateau-analysis");
    const progressDiv=document.getElementById("progress-analysis");
    const nextWeightsDiv=document.getElementById("next-weights");

    plateauDiv.innerHTML="<h3>Plateau Analysis</h3>";
    progressDiv.innerHTML="<h3>Progress Analysis</h3>";
    nextWeightsDiv.innerHTML="<h3>Next Suggested Weights</h3>";

    //plateau feedback
    for(const [exercise, isPlateau] of Object.entries(plateaus)){
        const p=document.createElement("p");
        p.textContent=exercise;
        if(isPlateau){
            p.textContent+= ": You have hit a plateau. Consider changing your routine.";
            p.className="plateau";
            p.prepend("⏸️ ");

        }else{
            p.textContent+= ": You are making progress!";
            p.className="progress";
            p.prepend("✅ ");
        }
        plateauDiv.appendChild(p);
    }

    //progress feedback
    for(const [exercise, diff] of Object.entries(progress)){
        const p=document.createElement("p");
        p.textContent=exercise;
        if(diff>0){
            p.textContent+= `: Your total volume increased by ${diff} lbs since last session. Great job!`;
            p.className="progress";
            p.prepend("📈 ");

        } else if(diff<0){
            p.textContent+= `: Your total volume decreased by ${Math.abs(diff)} lbs since last session. Keep pushing!`;
            p.className="regress";
            p.prepend("📉 ");
        }else{
            p.textContent+= ": No change in total volume since last session.";
            p.className="plateau";
            p.prepend("⏸️ ");

        }
        progressDiv.appendChild(p);
    } 
    //next weights suggestions
    for(const [exercise, weight] of Object.entries(nextWeights)){
        const p=document.createElement("p");
        p.textContent=`${exercise}: Next suggested weight is ${weight} lbs.`;
        p.className="progress";
        p.prepend("🏋️ ");
        nextWeightsDiv.appendChild(p);
}
}